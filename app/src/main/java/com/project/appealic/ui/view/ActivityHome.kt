package com.project.appealic.ui.view

import SongAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Song
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.ui.viewmodel.SongViewModelFactory
class ActivityHome : AppCompatActivity() {

    private lateinit var  songViewModel : SongViewModel

    private var songRepository: SongRepository = SongRepository()


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

        val factory = SongViewModelFactory(songRepository )
        songViewModel = ViewModelProvider(this,factory).get(SongViewModel::class.java)
        songViewModel.getAllArtists()
        songViewModel.getAllTracks()

        songViewModel.tracks.observe(this, Observer{
            tracks->
        })


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

        songViewModel.artists.observe(this, Observer { artists ->
            val listArtist : ArrayList<Artist> = ArrayList()
            for (artist in artists){
                listArtist.add(Artist(artist.Bio,artist.Name,artist.albums,artist.ImageResource))
                Log.d("info", listArtist.toString())
                val artistAdapter = ArtistAdapter(this,listArtist)
                recyclerViewArtists.adapter = artistAdapter
            }
        })



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this,
            R.color.bottom_nav_icon_selector
        ))



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
