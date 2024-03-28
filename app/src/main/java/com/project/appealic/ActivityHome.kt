package com.project.appealic

import SongAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.data.model.Song
import com.project.appealic.ui.view.Adapters.BannerAdapter

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Replace with your actual layout file name

        // Load banner home
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart, R.drawable.imagecart)
        val bannerAdapter = BannerAdapter(bannerImages)

        // Initialize and configure the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rrBanner)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // Set the layout manager
        recyclerView.adapter = bannerAdapter // Set the adapter

        // Load top songs
        val topSongs = listOf(
            Song(R.drawable.song1, "Song 1", "Singer 1"),
        )
        val listView = findViewById<ListView>(R.id.lvTopSong)
        val adapter = SongAdapter(this, topSongs)
        listView.adapter = adapter


//        Load bottom bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
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
                    // Handle Library item selection
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
