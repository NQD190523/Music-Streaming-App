package com.project.appealic.ui.view

import LibraryFragment
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.ui.view.Fragment.HomeFragment
import com.project.appealic.ui.view.Fragment.ProfileFragment
import com.project.appealic.ui.view.Fragment.SearchFragment
import com.project.appealic.ui.viewmodel.AuthViewModel

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

        // Khởi tạo AppWidgetManager
        appWidgetManager = AppWidgetManager.getInstance(this)

        // Lấy danh sách các widget trong ứng dụng của bạn
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WidgetView::class.java))

        // Kiểm tra xem có widget nào không
        if (appWidgetIds.isNotEmpty()) {
            // Lấy thông tin của WidgetProvider
            widgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetIds[0])

            // Tạo một AppWidgetHostView và cập nhật nó với thông tin từ AppWidgetProviderInfo
            val widgetHostView = AppWidgetHostView(this).apply {
                setAppWidget(appWidgetIds[0], widgetProviderInfo)
            }

            // Thêm AppWidgetHostView vào FrameLayout trong layout của Activity hoặc Fragment
            widgetContainer = findViewById(R.id.widgetContainer)
            widgetContainer.addView(widgetHostView)
        }
    }

    fun replaceFragment(fragment: Fragment) {
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
