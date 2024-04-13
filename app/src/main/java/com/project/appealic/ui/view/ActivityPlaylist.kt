package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.net.Uri

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment
import com.project.appealic.ui.view.Fragment.MusicControlFragment
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel

class ActivityPlaylist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.music_control_fragment_container, MusicControlFragment())
                .commit()
        }

        // Load các thông tin liên quan đến bài hát từ Intent và hiển thị trên giao diện
        val trackImage = intent.getStringExtra("TRACK_IMAGE")


        // Load hình ảnh sử dụng Glide
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            val songImageView = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        // Sự kiện khi nhấn nút back
        val backButton = findViewById<ImageView>(R.id.imv_dropdown)
        backButton.setOnClickListener {
            Back()
        }
    }

    private fun Back() {
        val sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        editor.putString("SONG_TITLE", songTitle)
        editor.putString("SINGER_NAME", artistName)
        editor.putString("TRACK_IMAGE", trackImage)

        editor.apply()

        // Cập nhật widget sau khi thông tin bài hát được lưu
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WidgetView::class.java))
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            WidgetView().onUpdate(this, appWidgetManager, appWidgetIds)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Lưu trạng thái của hoạt động nếu cần thiết
    }

    override fun onPause() {
        super.onPause()
        // Lưu trạng thái nếu cần thiết khi hoạt động bị pause
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dừng các hoạt động hoặc giải phóng tài nguyên khi hoạt động bị hủy
    }
}