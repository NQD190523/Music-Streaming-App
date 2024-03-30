package com.project.appealic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.ui.view.Adapters.BannerAdapter

class ActivityLibrary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        // Load banner
        val bannerImages = listOf(R.drawable.imagecard2, R.drawable.imagecard2, R.drawable.imagecard2)
        val bannerAdapter = BannerAdapter(bannerImages)

        // Initialize and configure the RecyclerView for banner
        val recyclerViewBanner = findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter


        // Load bottom bar
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_library
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle Home item selection
                    true
                }
                R.id.navigation_search -> {
                    // Handle Search item selection
                    true
                }
                R.id.navigation_library -> {
                    // Handle Library item selection (already selected)
                    true
                }
                R.id.navigation_profile -> {
                    // Handle Profile item selection
                    true
                }
                else -> false
            }
        }
    }
}
