package com.project.appealic.ui.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.repository.service.MusicPlayerService

import com.project.appealic.ui.view.Fragment.MusicControlFragment
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel


class ActivityPlaylist : AppCompatActivity() {
    private lateinit var trackId: String
    private lateinit var musicPlayerViewModel: MusicPlayerViewModel
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicBinder
            val musicService = binder.getService()
            // Thiết lập MusicService cho MusicPlayerViewModel
            musicPlayerViewModel.setMusicService(musicService)
            var player = musicPlayerViewModel.getPlayerInstance()!!
        }
        override fun onServiceDisconnected(className: ComponentName) {
            // Do nothing
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)
        val fragment = MusicControlFragment()
        val bundle = Bundle()

        bundle.putString("SONG_TITLE", intent.getStringExtra("SONG_TITLE"))
        bundle.putString("SINGER_NAME", intent.getStringExtra("SINGER_NAME"))
        bundle.putInt("DURATION", intent.getIntExtra("DURATION", 0))
        bundle.putString("TRACK_IMAGE", intent.getStringExtra("TRACK_IMAGE"))

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.music_control_fragment_container, fragment)
            .commit()


        val musicPlayerServiceIntent = Intent(this,MusicPlayerService::class.java)
        startService(musicPlayerServiceIntent)
        bindService(musicPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        musicPlayerViewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        // Load các thông tin liên quan đến bài hát từ Intent và hiển thị trên giao diện
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        val trackList = intent.getStringArrayListExtra("TRACK_LIST")
        trackId = intent.getStringExtra("TRACK_ID").toString()


        // Load hình ảnh sử dụng Glide
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            val songImageView = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        //Load dữ liệu audio
        val storage = Firebase.storage
        val storageRef = storage.reference
        if (trackList != null) {
            for (i in trackList){
                val trackPath = i?.substring(i.indexOf("/", 5) + 1)
                val audioRef = trackPath?.let { storageRef.child(it) }
                println(audioRef)
                if (audioRef != null) {
                    audioRef.downloadUrl.addOnSuccessListener { url ->
                        val songUri = Uri.parse(url.toString())
                        val mediaItems = mutableListOf<MediaItem>()
                        mediaItems.add(MediaItem.fromUri(songUri))
                        println(mediaItems)
                        musicPlayerViewModel.setMediaUri(mediaItems)
                    }
                }
            }
        }


        // Sự kiện khi nhấn nút back
        val backButton = findViewById<ImageView>(R.id.imv_dropdown)
        backButton.setOnClickListener {
            Back()
        }
    }

    private fun Back() {
        val sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        editor.putString("SONG_TITLE", songTitle)
        editor.putString("SINGER_NAME", artistName)
        editor.putString("TRACK_IMAGE", trackImage)

        editor.apply()

        // Cập nhật widget sau khi thông tin bài hát được lưu
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds =
            appWidgetManager.getAppWidgetIds(ComponentName(this, WidgetView::class.java))
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            WidgetView().onUpdate(this, appWidgetManager, appWidgetIds)
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Lưu trạng thái của hoạt động nếu cần thiết
    }

    override fun onPause() {
        super.onPause()
        // Lưu trạng thái nếu cần thiết khi hoạt động bị pause

    }

    override fun onDestroy() {
        super.onDestroy()
//        musicPlayerViewModel.pause()
    }
}







