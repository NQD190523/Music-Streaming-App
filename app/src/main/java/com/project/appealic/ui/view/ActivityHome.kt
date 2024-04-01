package com.project.appealic.ui.view

import SongAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.ui.viewmodel.SongViewModelFactory
class ActivityHome : AppCompatActivity() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listView: ListView
    private lateinit var recyclerViewArtists: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository())
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Khởi tạo và cấu hình RecyclerView cho banner
        val recyclerViewBanner = findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart, R.drawable.imagecart)
        val bannerAdapter = BannerAdapter(bannerImages)
        recyclerViewBanner.adapter = bannerAdapter

        // Khởi tạo và cấu hình ListView cho danh sách các bài hát mới
        listView = findViewById(R.id.lvNewRelease)
        songViewModel.tracks.observe(this, Observer { tracks ->
            val adapter = NewReleaseAdapter(this, tracks)
            listView.adapter = adapter
        })

        // Khởi tạo và cấu hình RecyclerView cho danh sách nghệ sĩ
        recyclerViewArtists = findViewById(R.id.recyclerViewArtist)
        recyclerViewArtists.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        songViewModel.artists.observe(this, Observer { artists ->
            val artistAdapter = ArtistAdapter(this, artists)
            recyclerViewArtists.adapter = artistAdapter
        })

        // Lấy danh sách tracks và artists từ repository
        songViewModel.getAllTracks()
        songViewModel.getAllArtists()

        // Xác định ListView
        listView = findViewById(R.id.lvNewRelease)

        // Thiết lập OnItemClickListener cho ListView
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Lấy dữ liệu của mục được chọn từ Adapter
                val selectedSong = parent.getItemAtPosition(position) as Track

                // Tạo Intent để chuyển sang ActivityPlaylist
                val intent = Intent(this, ActivityPlaylist::class.java)

                // Truyền dữ liệu cần thiết qua Intent
                intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
                intent.putExtra("SINGER_NAME", selectedSong.artistId)
                intent.putExtra("SONG_NAME", selectedSong.trackTitle)
                intent.putExtra("track_image", selectedSong.trackImage)



                // Bắt đầu ActivityPlaylist
                startActivity(intent)
            }

        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(
            ContextCompat.getColorStateList(
                this,
                R.color.bottom_nav_icon_selector
            )
        )
        bottomNavigationView.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
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
                else -> false
            }
        }
    }
}