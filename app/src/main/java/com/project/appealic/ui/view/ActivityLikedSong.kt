package com.project.appealic.ui.view

import SongAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ImageView

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.LikedSongsAdapter
import com.project.appealic.ui.view.Fragment.ProfileFragment
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class ActivityLikedSong : AppCompatActivity() {
    private lateinit var adapter: LikedSongsAdapter
    private lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_song)

        // Khởi tạo SongRepository và UserRepository
        val songRepository = SongRepository(application)
        val userRepository = UserRepository(application)

        // Tạo SongViewModelFactory với các repository đã khởi tạo
        val factory = SongViewModelFactory(songRepository, userRepository)
        songViewModel = ViewModelProvider(this, factory)[SongViewModel::class.java]

        adapter = LikedSongsAdapter(this, ArrayList())

        val lvLikedSongs: ListView = findViewById(R.id.lvLikedSongs)
        lvLikedSongs.adapter = adapter

        songViewModel.likedSongs.observe(this, Observer { songs ->
            adapter.clear()
            adapter.addAll(songs)
            adapter.notifyDataSetChanged()
            // Onclick sang music control
            lvLikedSongs.setOnItemClickListener(this,songViewModel,songs)
        })

        // Gọi phương thức để lấy danh sách các bài hát yêu thích
        songViewModel.getLikedSongs(FirebaseAuth.getInstance().currentUser?.uid.toString())




        findViewById<ImageView>(R.id.imv_back).setOnClickListener {
            finish()
        }
    }
}
