package com.project.appealic.ui.view

import com.project.appealic.ui.view.Fragment.LibraryFragment
import android.app.PendingIntent
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.view.Fragment.HomeFragment
import com.project.appealic.ui.view.Fragment.ProfileFragment
import com.project.appealic.ui.view.Fragment.SearchFragment
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import com.project.appealic.ui.viewmodel.AuthViewModel
import com.project.appealic.utils.ActivityContextSingleton

class ActivityHome : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var authViewModel: AuthViewModel
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val libraryFragment = LibraryFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var widgetContainer: FrameLayout
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var widgetProviderInfo: AppWidgetProviderInfo
    private lateinit var songViewModel: SongViewModel

    private val songBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Kiểm tra xem broadcast có phải là ACTION_NEW_SONG không
            if (intent?.action == "ACTION_NEW_SONG") {
                songViewModel.tracks.observe(this@ActivityHome, Observer {tracks ->
                    // Khởi tạo widget khi có bài hát mới được phát
                    initWidget(tracks[0].trackTitle.toString(), tracks[0].artist.toString(), tracks[0].trackImage.toString())
                })

            }
        }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    replaceFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_library -> {
                    replaceFragment(libraryFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    replaceFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        // Set default fragment
        replaceFragment(homeFragment)
        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(
            ContextCompat.getColorStateList(
                this,
                R.color.bottom_nav_icon_selector
            )
        )
        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
        songViewModel = ViewModelProvider(this, factory)[SongViewModel::class.java]

        // Khởi tạo AppWidgetManager
        appWidgetManager = AppWidgetManager.getInstance(this)

        ActivityContextSingleton.setActivityHomeContext(this)

        // Lấy danh sách các widget trong ứng dụng của bạn
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WidgetView::class.java))

        if (appWidgetIds.isNotEmpty()) {
            widgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetIds[0])

            val newFilter = IntentFilter("ACTION_NEW_SONG")
            registerReceiver(songBroadcastReceiver, newFilter, RECEIVER_NOT_EXPORTED)

            val changedFilter = IntentFilter("ACTION_SONG_CHANGED")
            LocalBroadcastManager.getInstance(this).registerReceiver(songBroadcastReceiver, changedFilter)

            val widgetHostView = AppWidgetHostView(this).apply {
                setAppWidget(appWidgetIds[0], widgetProviderInfo)
            }

            // Thêm AppWidgetHostView vào FrameLayout trong layout của Activity hoặc Fragment
            widgetContainer = findViewById(R.id.widgetContainer)
            widgetContainer.addView(widgetHostView)

            // Tạo một RemoteViews mới dựa trên layout của widget
            val remoteViews = RemoteViews(packageName, R.layout.widget_playsong)

            // Tạo Intent để gửi tới MusicPlayerService khi widget được click
            val playIntent = Intent(this, MusicPlayerService::class.java).apply {
                action = MusicPlayerService.ACTION_PLAY
            }
            val playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.imvPlay, playPendingIntent)

            val pauseIntent = Intent(this, MusicPlayerService::class.java).apply {
                action = MusicPlayerService.ACTION_PAUSE
            }
            val pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.imvPlay, pausePendingIntent)
            // Thiết lập RemoteViews mới cho widgetHostView
            widgetHostView.apply {
                val remoteViews = RemoteViews(packageName, R.layout.widget_playsong)
                setAppWidget(appWidgetIds[0], widgetProviderInfo)
                updateAppWidget(remoteViews)
            }
        }

    }
    private fun initWidget(songTitle: String, artistName: String, trackImage: String) {
        val remoteViews = RemoteViews(packageName, R.layout.widget_playsong)

        // Cập nhật thông tin bài hát trong widget
        remoteViews.setTextViewText(R.id.txtSongName, songTitle)
        remoteViews.setTextViewText(R.id.txtArtistName, artistName)

        val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage)
        Glide.with(this)
            .asBitmap()
            .load(gsReference)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    remoteViews.setImageViewBitmap(R.id.imvSongPhoto, resource)

                    // Cập nhật widget bằng cách gửi một broadcast
                    val updateIntent = Intent(this@ActivityHome, WidgetView::class.java)
                    updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    val ids = AppWidgetManager.getInstance(this@ActivityHome)
                        .getAppWidgetIds(ComponentName(this@ActivityHome, WidgetView::class.java))
                    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    sendBroadcast(updateIntent)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmenthome, fragment).commit()
    }

    internal fun onSeachOpen() {
        val view: View = bottomNavigationView.findViewById(R.id.navigation_search)
        view.performClick()
    }

    internal fun onSearchCancel() {
        val view: View = bottomNavigationView.findViewById(R.id.navigation_home)
        view.performClick()
    }
}
