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
        })

        // Gọi phương thức để lấy danh sách các bài hát yêu thích
        songViewModel.getLikedSongs(FirebaseAuth.getInstance().currentUser?.uid.toString())

        // Onclick sang music control
        lvLikedSongs.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Lấy dữ liệu của mục được chọn từ Adapter
            val selectedSong = parent.getItemAtPosition(position) as Track
            //lưu bài hát vừa mở vào database của thiết bị
            val user = FirebaseAuth.getInstance().currentUser?.uid
            val intent = Intent(this, ActivityMusicControl::class.java)
            val trackUrlList = java.util.ArrayList<String>()

            val song = selectedSong.trackId?.let {
                SongEntity(
                    it,
                    selectedSong.trackImage,
                    selectedSong.trackTitle,
                    selectedSong.artist,
                    user,
                    null,
                    System.currentTimeMillis(),
                    null
                )
            }

            if (song != null) {
                songViewModel.insertSong(song)
                Log.d(" test status", "success")
            }

//              Lấy dữ liệu các url trong playlist
            for (i  in 0 until parent.count){
                val item = parent.getItemAtPosition(i) as Track
                item.trackUrl?.let { trackUrl ->
                    println(trackUrl)
                    trackUrlList.add(trackUrl)
                }
            }
            println(position)
            // Truyền dữ liệu cần thiết qua Intent
            intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
            intent.putExtra("SINGER_NAME", selectedSong.artist)
            intent.putExtra("SONG_NAME", selectedSong.trackTitle)
            intent.putExtra("TRACK_IMAGE", selectedSong.trackImage)
            intent.putExtra("ARTIST_ID", selectedSong.artistId)
            intent.putExtra("DURATION", selectedSong.duration)
            intent.putExtra("TRACK_URL", selectedSong.trackUrl)
            intent.putExtra("TRACK_ID", selectedSong.trackId)
            intent.putExtra("TRACK_INDEX",position)
            intent.putStringArrayListExtra("TRACK_LIST",trackUrlList)
            startActivity(intent)
        }


        findViewById<ImageView>(R.id.imv_back).setOnClickListener {
            finish()
        }
    }
}
