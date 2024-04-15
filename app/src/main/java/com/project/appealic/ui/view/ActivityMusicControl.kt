package com.project.appealic.ui.view

import android.app.Dialog
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment
import com.project.appealic.ui.view.Fragment.PlaySongFragment
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel

class ActivityMusicControl : AppCompatActivity(){

    private var isRepeating = false
    private var playlist: ArrayList<MediaBrowser.MediaItem> = ArrayList()
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
    private lateinit var playerNotificationManager: PlayerNotificationManager


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
        setContentView(R.layout.activity_music_control)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.play_fragment_container, PlaySongFragment())
                .commit()
        }

        val musicPlayerServiceIntent = Intent(this,MusicPlayerService::class.java).apply {
            action = MusicPlayerService.ACTION_PLAY
        }
        startService(musicPlayerServiceIntent)
        bindService(musicPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        musicPlayerViewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]



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
        val trackList = intent.getStringArrayListExtra("TRACK_LIST")
        val trackIndex = intent.getIntExtra("TRACK_INDEX",0)
        trackId = intent.getStringExtra("TRACK_ID").toString()

        findViewById<TextView>(R.id.song_name).text = songTitle
        findViewById<TextView>(R.id.singer_name).text = artistName
        // Load hình ảnh sử dụng Glide
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val songImageView = findViewById<ImageView>(R.id.imvGround)

                Glide.with(this)
                    .load(uri)
                    .into(songImageView)
            }
        }

        try {
            //Load dữ liệu audio
            val storage = Firebase.storage
            val storageRef = storage.reference
            val mediaItems = mutableListOf<MediaItem>()
            // Số lượng URL download đã hoàn thành
            var completedDownloads = 0

            if (trackList != null) {
                for (i in trackList){
                    val trackPath = i?.substring(i.indexOf("/", 5) + 1)
                    val audioRef = trackPath?.let { storageRef.child(it) }
                    println(audioRef)
                    if (audioRef != null) {
                        audioRef.downloadUrl.addOnSuccessListener { url ->
                            val songUri = Uri.parse(url.toString())
                            mediaItems.add(MediaItem.fromUri(songUri))
                            println(mediaItems)
                            // Tăng số lượng URL download đã hoàn thành
                            completedDownloads++
                            // Nếu đã download xong tất cả các URL
                            if (completedDownloads == trackList.size) {
                                // Set danh sách media items cho ExoPlayer
                                musicPlayerViewModel.setMediaUri(mediaItems, trackIndex)
                            }
                        }.addOnFailureListener { exception ->
                            // Xử lý khi có lỗi xảy ra trong quá trình download
                            Log.e("Error", "Failed to download track: ${exception.message}")
                        }
                    }
                }
            }
        } catch (e : Exception){
            Log.e("Error", e.message.toString())
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



    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Back()
    }
    fun Back() {
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

    private fun handleMixButtonClick() {

    }

    // Các hàm xử lý sự kiện khi nhấn các nút
    fun handlePreviousButtonClick() {
       musicPlayerViewModel.previousButtonClick()
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
        musicPlayerViewModel.nextButtonClick()
    }

    private fun handleRepeatButtonClick() {
        musicPlayerViewModel.repeatButtonClick()
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

private fun formatDuration(durationInSeconds: Long): String {
    val seconds = (durationInSeconds / 1000) % 60
    val minutes = durationInSeconds / 60000
    return "$minutes:${String.format("%02d", seconds)}"
}

