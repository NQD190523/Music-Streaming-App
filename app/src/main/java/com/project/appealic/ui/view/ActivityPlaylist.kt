package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri

import android.os.Bundle
import android.os.IBinder
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
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel
import com.project.appealic.utils.MusicPlayerViewModelFactory

class ActivityPlaylist : AppCompatActivity() {

    private var isRepeating = false
    private var playlist: ArrayList<MediaItem> = ArrayList()
    private var currentTrackIndex: Int = 0
    private lateinit var progressTv: TextView
    private lateinit var progressSb: SeekBar
    private lateinit var durationTv: TextView
    private lateinit var previousBtn: ImageView
    private lateinit var playBtn: ImageView
    private lateinit var mixBtn: ImageView
    private lateinit var playFrameLayout: FrameLayout
    private lateinit var nextBtn: ImageView
    private lateinit var repeatBtn: ImageView
    private lateinit var commentBtn: ImageView
    private lateinit var downloadBtn: ImageView
    private lateinit var moreBtn: Button
    private lateinit var addPlaylistBtn: ImageView
    private lateinit var shareBtn: ImageView
    private  var player: ExoPlayer? = null
    private lateinit var trackId: String
    private lateinit var musicPlayerViewModel: MusicPlayerViewModel

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicBinder
            val musicService = binder.getService()

            // Thiết lập MusicService cho MusicPlayerViewModel
            musicPlayerViewModel.setMusicService(musicService)
            player = musicPlayerViewModel.getPlayerInstance()!!
        }
        override fun onServiceDisconnected(className: ComponentName) {
            // Do nothing
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)

        val musicPlayerServiceIntent = Intent(this,MusicPlayerService::class.java)
        startService(musicPlayerServiceIntent)
        bindService(musicPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        musicPlayerViewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        //Lấy trạng thái trc khi thoát của audio
//        if (savedInstanceState != null) {
//            val savedPosition = musicPlayerViewModel.getAudioPosition(trackId)
//            savedPosition?.let {
//                player.seekTo(savedPosition)
//            }
//        }

        // Khởi tạo tất cả các thành phần UI
        progressTv = findViewById(R.id.progressTv)
        progressSb = findViewById(R.id.progressSb)
        durationTv = findViewById(R.id.durationTv)
        previousBtn = findViewById(R.id.previous)
        mixBtn = findViewById(R.id.mix)
//        playFrameLayout = findViewById(R.id.playPause)
        nextBtn = findViewById(R.id.next)
        repeatBtn = findViewById(R.id.repeat)
        commentBtn = findViewById(R.id.comment)
        downloadBtn = findViewById(R.id.dowmload)
        moreBtn = findViewById(R.id.more)
        addPlaylistBtn = findViewById(R.id.addPlaylist)
        shareBtn = findViewById(R.id.share)
        playBtn = findViewById(R.id.playPauseIcon)


        // Lấy dữ liệu từ Intent và hiển thị trên giao diện
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        val duration = intent.getIntExtra("DURATION", 0)
        val trackUrl = intent.getStringExtra("TRACK_URL")
        trackId = intent.getStringExtra("TRACK_ID").toString()

        findViewById<TextView>(R.id.song_name).text = songTitle
        findViewById<TextView>(R.id.singer_name).text = artistName

        val backButton = findViewById<ImageView>(R.id.imv_dropdown)
        backButton.setOnClickListener {
            Back()
        }
        // Load hình ảnh sử dụng Glide
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            val songImageView = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        //Load dữ liệu audio
        val storage = Firebase.storage
        val storageRef = storage.reference
        val trackPath = trackUrl?.substring(trackUrl.indexOf("/", 5) + 1)
        val audioRef = trackPath?.let { storageRef.child(it) }
        println(audioRef)
        if (audioRef != null) {
            audioRef.downloadUrl.addOnSuccessListener { url ->
                val songUri = Uri.parse(url.toString())
                musicPlayerViewModel.setMediaUri(songUri)
            }
        }
        // Gắn các hàm xử lý sự kiện cho các thành phần UI
        previousBtn.setOnClickListener { handlePreviousButtonClick() }
        mixBtn.setOnClickListener { handleMixButtonClick() }
        nextBtn.setOnClickListener { handleNextButtonClick() }
        repeatBtn.setOnClickListener { handleRepeatButtonClick() }
        commentBtn.setOnClickListener { handleCommentButtonClick() }
        downloadBtn.setOnClickListener { handleDownloadButtonClick() }
        moreBtn.setOnClickListener { handleMoreButtonClick() }
        addPlaylistBtn.setOnClickListener { handleAddPlaylistButtonClick() }
        shareBtn.setOnClickListener { handleShareButtonClick() }
        playBtn.setOnClickListener { handelPlayButtonClick() }

        //Cập nhật trang thái khi thay đổi progresBar
        progressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Xử lý sự kiện thay đổi tiến trình
                if (fromUser) {
                    player?.seekTo((progress * 1000).toLong())
                    progressTv.text = formatDuration(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi bắt đầu chạm vào SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi kết thúc chạm vào SeekBar
            }
        })

        //ProgressBar cập nật theo tiến độ của bài hát
        progressSb.max = duration / 1000
        musicPlayerViewModel.getCurrentPositionLiveData().observe(this) { curentPosition ->
            progressSb.progress = (curentPosition / 1000).toInt()
            progressTv.text = formatDuration(curentPosition)
            val remainingDuration = (duration - curentPosition)
            durationTv.text = formatDuration(remainingDuration)
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
        // Save other song info to SharedPreferences if needed
        editor.apply()

        // Update the widget after song info is saved
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds =
            appWidgetManager.getAppWidgetIds(ComponentName(this, WidgetView::class.java))
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            WidgetView().onUpdate(this, appWidgetManager, appWidgetIds)
        }

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
//        musicPlayerViewModel.pause()
    }

    private fun handleMixButtonClick() {

    }

    private fun formatDuration(durationInSeconds: Long): String {
        val seconds = (durationInSeconds / 1000) % 60
        val minutes = durationInSeconds / 60000
        return "$minutes:${String.format("%02d", seconds)}"
    }

    // Các hàm xử lý sự kiện khi nhấn các nút
    fun handlePreviousButtonClick() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
            player?.setMediaItem(playlist[currentTrackIndex])
            player?.prepare()
            player?.play()
        }
    }

    private fun handelPlayButtonClick() {
        if (player?.isPlaying == true) {
            player?.pause()
            playBtn.setImageResource(R.drawable.ic_play_24_filled)
        } else {
            player?.play()
            playBtn.setImageResource(R.drawable.ic_pause_24_filled)
        }
    }

    private fun handleNextButtonClick() {
        if (currentTrackIndex < playlist.size - 1) {
            currentTrackIndex++
            player?.setMediaItem(playlist[currentTrackIndex])
            player?.prepare()
            player?.play()
        }
    }

    private fun handleRepeatButtonClick() {
        if (player?.isPlaying == true) {
            isRepeating = !isRepeating
            player?.repeatMode = if (isRepeating) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }
    private fun handleCommentButtonClick() {
        // Xử lý khi nhấn nút Comment
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_comment, null)

        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)

        dialog.setContentView(view)
        dialog.show()
    }

        private fun handleDownloadButtonClick() {
            // Xử lý khi nhấn nút Download
        }


    private fun handleMoreButtonClick() {
        val moreActionFragment = MoreActionFragment()
        val bundle = Bundle()
        bundle.putString("SONG_TITLE", intent.getStringExtra("SONG_TITLE"))
        bundle.putString("SINGER_NAME", intent.getStringExtra("SINGER_NAME"))
        bundle.putString("TRACK_IMAGE", intent.getStringExtra("TRACK_IMAGE"))
        moreActionFragment.arguments = bundle
        moreActionFragment.show(supportFragmentManager, "MoreActionsFragment")
    }

    private fun handleAddPlaylistButtonClick() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(supportFragmentManager, "AddPlaylistFragment")
    }

    private fun handleShareButtonClick() {
    }

}

