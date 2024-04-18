package com.project.appealic.ui.view

import SongAdapter
import android.os.Bundle
import android.widget.ImageView

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.LikedSongsAdapter
import com.project.appealic.ui.view.Fragment.ProfileFragment
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class ActivityLikedSong : AppCompatActivity() {
    private lateinit var viewModel: SongViewModel
    private lateinit var adapter: LikedSongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_song)

        // Khởi tạo SongRepository và UserRepository
        val songRepository = SongRepository(application)
        val userRepository = UserRepository(application)

        // Tạo SongViewModelFactory với các repository đã khởi tạo
        val factory = SongViewModelFactory(songRepository, userRepository)

        // Sử dụng ViewModelProvider với SongViewModelFactory để tạo SongViewModel
        viewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
        adapter = LikedSongsAdapter(this, R.layout.activity_liked_song, ArrayList())

        val listView: ListView = findViewById(R.id.lvLikedSongs)
        listView.adapter = adapter

        viewModel.likedSongs.observe(this, Observer { songs ->
            adapter.clear()
            adapter.addAll(songs)
            adapter.notifyDataSetChanged()
        })

        // Gọi phương thức để lấy danh sách các bài hát yêu thích
        viewModel.getLikedSongs(FirebaseAuth.getInstance().currentUser?.uid.toString())

        findViewById<ImageView>(R.id.imv_back).setOnClickListener {
            finish()
        }
    }
}
