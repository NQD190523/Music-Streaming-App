package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.ui.view.Adapters.BannerAdapter

class ActivityLibrary : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        // Load banner
        val bannerImages = listOf(
            R.drawable.imagecard2,
            R.drawable.imagecard2,
            R.drawable.imagecard2
        )
        val bannerAdapter = BannerAdapter(bannerImages)

        // Initialize and configure the RecyclerView for banner
        val recyclerViewBanner = findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter


        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_selector))
        bottomNavigationView.selectedItemId = R.id.navigation_library
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
                R.id.navigation_library ->
                    true
                R.id.navigation_profile -> {
                    val intent = Intent(this, ActivityProfile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
