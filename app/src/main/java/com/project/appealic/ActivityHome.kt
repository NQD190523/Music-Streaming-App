package com.project.appealic

import ActivityLibrary
import SongAdapter
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Song
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Replace with your actual layout file name
        // Load banner home
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart, R.drawable.imagecart)
        val bannerAdapter = BannerAdapter(bannerImages)

        // Initialize and configure the RecyclerView for banner
        val recyclerViewBanner = findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter


// Load top songs
        val topSongs = listOf(
            Song(R.drawable.song1, "Song 1", "Singer 1"),
        )
        val listView = findViewById<ListView>(R.id.lvTopSong)
        val songAdapter = SongAdapter(this, topSongs)
        listView.adapter = songAdapter
//Load recent songs recycler view
        val recentSongs = listOf(
            Song(R.drawable.song1, "Song 1", "Singer 1"),
        )
        val recyclerViewRecentSongs: RecyclerView = findViewById(R.id.RecentlyViewSong)
        recyclerViewRecentSongs.layoutManager = LinearLayoutManager(this)
        val songAdapterRecent = SongAdapter(this, recentSongs)

// Load top artists
        val recyclerViewArtists: RecyclerView = findViewById(R.id.recyclerViewArtist)
        recyclerViewArtists.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val artists = listOf(
            Artist("Artist 1", R.drawable.artist1),
            Artist("Artist 2", R.drawable.artist2),
            Artist("Artist 3", R.drawable.artist3),
            Artist("Artist 4", R.drawable.artist4),
        )
        val artistAdapter = ArtistAdapter(artists)
        recyclerViewArtists.adapter = artistAdapter



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_selector))



        // Cập nhật icon được chọn dựa trên Activity hiện tại
        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Đã ở trang Home, không cần chuyển đổi
                    true
                }
                R.id.navigation_search -> {
                    val intent = Intent(this, ActivitySearch::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_library -> {
                    val intent = Intent(this, ActivityLibrary::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ActivityProfile::class.java)
                    startActivity(intent)
                    true
                }
                // Thêm các case khác cho các mục khác
                else -> false
            }
        }
    }
}
