package com.project.appealic.ui.view

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerNotificationManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.InfoMusicFragment
import com.project.appealic.ui.view.Fragment.LyrisFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment
import com.project.appealic.ui.view.Fragment.PlaySongFragment
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory



class ActivityMusicControl : AppCompatActivity(){

    private lateinit var songViewModel: SongViewModel
    private lateinit var progressTv: TextView
    private lateinit var progressSb: SeekBar
    private lateinit var durationTv: TextView
    private lateinit var previousBtn: ImageView
    private lateinit var playBtn: ImageView
    private lateinit var mixBtn: ImageView
    private lateinit var nextBtn: ImageView
    private lateinit var repeatBtn: ImageView
    private lateinit var commentBtn: ImageView
    private lateinit var downloadBtn: ImageView
    private lateinit var moreBtn: Button
    private lateinit var addPlaylistBtn: ImageView
    private lateinit var shareBtn: ImageView
    private lateinit var likeBtn: ImageView
    private  var player: ExoPlayer? = null
    private lateinit var trackId: String
    private lateinit var musicPlayerViewModel: MusicPlayerViewModel
    private lateinit var trackList : ArrayList<String>
    private  var trackIndex : Int =0
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var songTitle : String
    private lateinit var artistName : String
    private lateinit var trackImage : String
    private  var duration : Int = 0
    private  var isRepeated : Boolean = false


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicBinder
            val musicService = binder.getService()
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

        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(this.application), UserRepository(this.application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
        musicPlayerViewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]


        // Lấy dữ liệu từ Intent và hiển thị trên giao diện
        songTitle = intent.getStringExtra("SONG_TITLE")?: "N/A"
        artistName = intent.getStringExtra("SINGER_NAME").toString()
        trackImage = intent.getStringExtra("TRACK_IMAGE").toString()
        duration = intent.getIntExtra("DURATION", 0)
        trackList = intent.getStringArrayListExtra("TRACK_LIST")!!
        trackIndex = intent.getIntExtra("TRACK_INDEX",0)
        trackId = intent.getStringExtra("TRACK_ID").toString()


        Log.d("MusicControlActivity", "Song title: $songTitle, Artist name: $artistName, Track image: $trackImage")
        val playSongFragment = PlaySongFragment.newInstance(trackImage ?: "")

        // Thiết lập ViewPager
        val viewLyrics = findViewById<View>(R.id.viewlyris)
        val viewInfoMusic = findViewById<View>(R.id.viewinfomusic)
        val viewSong = findViewById<View>(R.id.viewsong)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val fragments = listOf(InfoMusicFragment(), playSongFragment, LyrisFragment())

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        viewPager.adapter = adapter
        viewPager.currentItem = 1

        // Thiết lập các sự kiện nhấp vào View
        viewInfoMusic.setOnClickListener { viewPager.currentItem = 0 }
        viewSong.setOnClickListener { viewPager.currentItem = 1 }
        viewLyrics.setOnClickListener { viewPager.currentItem = 2 }


        val musicPlayerServiceIntent = Intent(this, MusicPlayerService::class.java)
        startService(musicPlayerServiceIntent)
        bindService(musicPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)


        songViewModel.getTrackByUrl(trackList[trackIndex])
        songViewModel.recentTrack.observe(this, Observer {track ->
            findViewById<TextView>(R.id.song_name).text = track[0].trackTitle
            findViewById<TextView>(R.id.singer_name).text = track[0].artist
            trackId = track[0].trackId.toString()
            duration = track[0].duration!!
            if (track[0].trackImage?.isNotEmpty() == true) {
                val gsReference = track[0].trackImage?.let { storage.getReferenceFromUrl(it) }
                Glide.with(this@ActivityMusicControl).load(gsReference).into(findViewById<ImageView>(R.id.imvGround))
                }
            })



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
        likeBtn = findViewById(R.id.like)




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
        playBtn.setOnClickListener { handlePlayButtonClick() }
        likeBtn.setOnClickListener { handleLikeButtonClick()}

        // Xét hiển thị bài hát yêu thích
        if (userId != null) {
            songViewModel.getLikedSongs(userId)
            songViewModel.likedSongs.observe(this, Observer { likedSong ->
                for (i in likedSong.indices) {
                    if (likedSong[i].trackId == trackId)
                        likeBtn.setImageResource(R.drawable.ic_isliked)
                    else likeBtn.setImageResource(R.drawable.ic_heart_24_outlined)
                }
            })
        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(this, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

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

        player?.addListener(object :Player.Listener{
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val newIndex = player!!.currentMediaItemIndex // Lấy chỉ số của bài hát mới
                // Cập nhật ViewModel với bài hát mới
                println(newIndex)
                songViewModel.getTrackByUrl(trackList[newIndex])
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadDataFromFirebase()
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        // Perform any final cleanup before the Activity is destroyed
        Back()
        super.onDestroy()
    }

    fun Back() {
        val sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        val artistId = intent.getStringExtra("ARTIST_ID")
        val trackId = intent.getStringExtra("TRACK_ID")
        editor.putString("SONG_TITLE", songTitle)
        editor.putString("SINGER_NAME", artistName)
        editor.putString("TRACK_IMAGE", trackImage)
        editor.putString("ARTIST_ID", artistId)
        editor.putString("TRACK_ID", trackId)
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
        val index = player?.currentMediaItemIndex
        songViewModel.getTrackByUrl(trackList[index!!])
        println(trackList[index])
    }

    private fun handlePlayButtonClick() {
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
        val index = player?.currentMediaItemIndex
        songViewModel.getTrackByUrl(trackList[index!!])

    }

    private fun handleRepeatButtonClick() {
        isRepeated = !isRepeated
        musicPlayerViewModel.repeatButtonClick()
        if(isRepeated)
            repeatBtn.setImageResource(R.drawable.ic_repeat_off_24_filled)
        else
            repeatBtn.setImageResource(R.drawable.ic_repeat_24_filled)


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

    private fun handleLikeButtonClick() {
        println(userId)
        if (userId != null) {
            songViewModel.getLikedSongs(userId)
            val isLiked = songViewModel.likedSongs.value?.any { it.trackId == trackId } ?: false
            println(isLiked)
            println(songViewModel.likedSongs.value)
            if (isLiked) {
                // Bài hát đã được thích, nên xóa khỏi danh sách yêu thích của người dùng
                songViewModel.removeTrackFromUserLikedSongs(userId, trackId)
                // Cập nhật trạng thái của likeBtn
                likeBtn.setImageResource(R.drawable.ic_heart_24_outlined)
            } else {
                // Bài hát chưa được thích, nên thêm vào danh sách yêu thích của người dùng
                songViewModel.addTrackToUserLikedSongs(userId, trackId)
                // Cập nhật trạng thái của likeBtn
                likeBtn.setImageResource(R.drawable.ic_isliked)
            }
        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(this, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleMoreButtonClick() {
        val moreActionFragment = MoreActionFragment()
        songViewModel.getTrackByUrl(trackList[trackIndex])
        val bundle = Bundle()
        bundle.putString("SONG_TITLE", intent.getStringExtra("SONG_TITLE"))
        bundle.putString("SINGER_NAME", intent.getStringExtra("SINGER_NAME"))
        bundle.putString("TRACK_IMAGE", intent.getStringExtra("TRACK_IMAGE"))
        bundle.putString("ARTIST_ID", intent.getStringExtra("ARTIST_ID"))
        bundle.putString("TRACK_ID", intent.getStringExtra("TRACK_ID"))
        moreActionFragment.arguments = bundle
        moreActionFragment.show(supportFragmentManager, "MoreActionsFragment")
    }


    private fun handleAddPlaylistButtonClick() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(supportFragmentManager, "AddPlaylistFragment")
    }

    private fun handleShareButtonClick() {
    }
    private fun loadDataFromFirebase() {
        // Load audio data from Firebase Storage
        val mediaItems = mutableListOf<MediaItem>()
        val tasks = mutableListOf<Task<Uri>>()

        for (i in 0 until trackList.size) {
            val trackPath = trackList[i]
            val audioRef = storageRef.child(trackPath.substring(trackPath.indexOf("/", 5) + 1))
            val task = audioRef.downloadUrl
            tasks.add(task)
        }
        // Chờ tất cả các tác vụ tải xuống hoàn tất
        Tasks.whenAllComplete(tasks).addOnCompleteListener { completedTasks ->
            val results = mutableListOf<Task<*>>()
            for (taskResult in completedTasks.result!!) {
                results.add(taskResult)
            }
            val size = results.size

            for (i in 0 until size) {
                val task = tasks[i]
                val index = i
                if (task.isSuccessful) {
                    val url = task.result as Uri
                    val songUri = MediaItem.fromUri(url)
                    mediaItems.add(songUri)

                    // Kiểm tra nếu đã tải xuống tất cả các track
                    if (mediaItems.size == trackList.size) {
                        musicPlayerViewModel.setMediaUri(mediaItems, trackIndex)
                    }
                } else {
                    Log.e("Error", "Failed to download track at index $index: ${task.exception?.message}")
                }
            }
        }
    }

    private fun formatDuration(durationInSeconds: Long): String {
        val seconds = (durationInSeconds / 1000) % 60
        val minutes = durationInSeconds / 60000
        return "$minutes:${String.format("%02d", seconds)}"
    }
}

