package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R

class ActivityProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wel_account)

        // Ánh xạ các thành phần trong layout
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayoutprofile)
        val newContent = findViewById<LinearLayout>(R.id.newContent)

        // Xử lý sự kiện để ẩn/hiện newContent khi frameLayout được nhấn
        frameLayout.setOnTouchListener { view, motionEvent ->
            // Tạo một LinearLayout.LayoutParams mới
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Thiết lập các thuộc tính của layoutParams nếu cần
            layoutParams.marginStart = 16 // Ví dụ

            // Gán layoutParams cho newContent
            newContent.layoutParams = layoutParams

            // Trả về true để chỉ ra rằng sự kiện đã được xử lý
            true
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_selector)
        bottomNavigationView.selectedItemId = R.id.navigation_profile
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, ActivityHome::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_search -> {
                    val intent = Intent(this, ActivitySearch::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_library -> {
                    val intent = Intent(this, ActivitySearch::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_profile -> true
                else -> false
            }
        }
    }
}
