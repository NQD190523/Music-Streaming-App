package com.project.appealic.ui.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.R
import com.squareup.picasso.Picasso

class ActivityPlaylist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)

        // Nhận dữ liệu từ Intent
        val intent = intent
        val songTitle = intent.getStringExtra("song_title")
        val songName = intent.getStringExtra("song_name")
        val singerName = intent.getStringExtra("artist_id") // Corrected key
        val trackImage = intent.getStringExtra("track_image")

        // Hiển thị thông tin trên giao diện người dùng
        val songTitleTextView = findViewById<TextView>(R.id.song_title)
        val singerNameTextView = findViewById<TextView>(R.id.singer_name)
        val songNameTextView = findViewById<TextView>(R.id.song_name)
        val trackImageView = findViewById<ImageView>(R.id.imageViewBackground)

        // Set text
        songTitleTextView.text = songTitle
        singerNameTextView.text = singerName
        songNameTextView.text = songName

        // Load image using Picasso
        Picasso.get().load(trackImage).into(trackImageView)
    }
}