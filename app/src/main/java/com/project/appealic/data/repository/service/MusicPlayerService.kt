package com.project.appealic.data.repository.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.ActivityMusicControl
import com.project.appealic.ui.viewmodel.SongViewModel

class MusicPlayerService : Service() {
    private lateinit var player: ExoPlayer
    private val binder = MusicBinder()
    val currentPositionLiveData = MutableLiveData<Long>()
    private val handler = Handler(Looper.getMainLooper())
    private val updateCurrentPositionRunnable = Runnable {
        updateCurrentPosition()
    }
    private var isRepeating = false
    private lateinit var notificationManager: NotificationManager
    private val _serviceReady = MutableLiveData<Boolean>()
    val serviceReady: LiveData<Boolean> = _serviceReady
    private lateinit var mediaSession: MediaSessionCompat
    private val _mediaIndexLiveData = MutableLiveData<Int>()
    val mediaIndexLiveData: LiveData<Int> = _mediaIndexLiveData
    companion object {
        private const val NOTIFICATION_ID = 1
        const val ACTION_PLAY = "com.project.appealic.action.PLAY"
        const val ACTION_PAUSE = "com.project.appealic.action.action.PAUSE"
        const val ACTION_STOP = "com.project.appealic.action.action.STOP"
        const val ACTION_NEXT = "com.project.appealic.action.action.NEXT"
        const val ACTION_PREVIOUS = "com.project.appealic.action.action.PREVIOUS"
        private const val CHANNEL_ID = "123"
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        ViewModelProvider.Factory
        // Khởi tạo mediaSession
        mediaSession = MediaSessionCompat(this, "MusicService")
        // Đặt callback cho mediaSession
        mediaSession.setCallback(mediaSessionCallback)
        // Đặt mediaSession là hoạt động chính
        mediaSession.isActive = true
        player = ExoPlayer.Builder(this).build()
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (mediaItem != null) {
                    updateTrackInfoOnUI(mediaItem)
                    _mediaIndexLiveData.postValue(player.currentMediaItemIndex)
                }
            }
        })
        trackCurrentPosition()
        // Khởi tạo NotificationManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Tạo kênh thông báo
        createNotificationChannel()
        // Bắt đầu dịch vụ
        startForeground(MusicPlayerService.NOTIFICATION_ID,createNotification())
        _serviceReady.value = true
    }
    private fun updateTrackInfoOnUI(mediaItem: MediaItem) {
        val songTitle = mediaItem.mediaMetadata.title.toString()
        val artistName = mediaItem.mediaMetadata.artist.toString()

        val intent = Intent("ACTION_TRACK_INDEX_CHANGED").apply {
            putExtra("songTitle", songTitle)
            putExtra("artistName", artistName)
            putExtra("TRACK_INDEX", player.currentMediaItemIndex)
        }
        sendBroadcast(intent)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val track = intent?.getParcelableExtra<Track>("songData")
        when (intent?.action) {
            ACTION_PLAY -> {
                // Xử lý yêu cầu play
                player.play()
                updateNotification()
            }
            ACTION_PAUSE -> {
                // Xử lý yêu cầu pause
                player.pause()
                updateNotification()
            }
            ACTION_NEXT -> {
                nextButtonClick()
                updateNotification()
            }
            ACTION_PREVIOUS -> {
                previousButtonClick()
                updateNotification()
            }

            ACTION_STOP -> {
                // Xử lý yêu cầu stop
                stopSelf() // Dừng service khi người dùng nhấn stop
            }
        }
        return START_NOT_STICKY
    }
    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            // Xử lý yêu cầu play
            player.play()
            updateNotification()
        }

        override fun onPause() {
            // Xử lý yêu cầu pause
            player.pause()
            updateNotification()
        }

        // Các phương thức khác như onSkipToNext, onSkipToPrevious...
    }

    fun setMediaUri(uri: MutableList<MediaItem>, startIndex : Int) {
        player.setMediaItems(uri)
        player.prepare()
        player.seekToDefaultPosition(startIndex)
        player.play()
        val intent = Intent("ACTION_NEW_SONG")
        sendBroadcast(intent)
    }

    fun play() {
        if(player.isPlaying){
            player.playWhenReady = true
        }
    }
    fun pause() {
        if (player.isPlaying) {
            player.playWhenReady = false
        }
    }


    fun nextButtonClick() {
        // Kiểm tra xem player có được khởi tạo không
        if (player.playbackState != Player.STATE_IDLE && player.playbackState != Player.STATE_ENDED) {
            // Lấy vị trí của mục phương tiện hiện tại trong danh sách phát
            val currentMediaItemIndex = player.currentMediaItemIndex
            // Xác định vị trí của mục phương tiện kế tiếp
            val nextMediaItemIndex = if (currentMediaItemIndex < player.mediaItemCount - 1) currentMediaItemIndex + 1 else currentMediaItemIndex
            // Chuyển đến mục phương tiện kế tiếp
            player.seekToDefaultPosition(nextMediaItemIndex)
            // Lấy thông tin về bài hát mới
            val nextMediaItem = player.getMediaItemAt(nextMediaItemIndex)
            // Gửi thông tin về bài hát mới đến ActivityMusicControl
            updateTrackInfoOnUI(nextMediaItem)
        }
    }
    fun previousButtonClick() {
        // Kiểm tra xem player có được khởi tạo không
        if (player.playbackState != Player.STATE_IDLE && player.playbackState != Player.STATE_ENDED) {
            // Lấy vị trí của mục phương tiện hiện tại trong danh sách phát
            val currentMediaItemIndex = player.currentMediaItemIndex
            // Xác định vị trí của mục phương tiện trước đó
            val previousMediaItemIndex = if (currentMediaItemIndex > 0) currentMediaItemIndex - 1 else currentMediaItemIndex
            // Chuyển đến mục phương tiện trước đó
            player.seekToDefaultPosition(previousMediaItemIndex)
        }
    }
    fun repeatButtonClick() {
        if (player.isPlaying) {
            isRepeating = !isRepeating
            player.repeatMode = if (isRepeating) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    fun getExoPlayerInstance(): ExoPlayer {
        return player
    }
    fun stopPlayer() {
        player.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
    private fun updateCurrentPosition() {
        val currentPosition = player.currentPosition
        currentPositionLiveData.postValue(currentPosition)
        // Gửi lại một Runnable sau mỗi 1 giây
        handler.postDelayed(updateCurrentPositionRunnable, 1000)
    }
    // Hàm để theo dõi vị trí hiện tại của ExoPlayer và cập nhật LiveData
    private fun trackCurrentPosition() {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                // Nếu trạng thái là PLAYING, bắt đầu cập nhật vị trí
                if (state == Player.STATE_READY && player.playWhenReady) {
                    updateCurrentPosition()
                } else {
                    // Nếu không phát hoặc đã dừng, dừng cập nhật
                    handler.removeCallbacks(updateCurrentPositionRunnable)
                }
            }
        })
    }
    fun getCurrentPositionLiveData(): LiveData<Long> {
        return currentPositionLiveData
    }
    private fun createNotification(): Notification {
        // Tạo Intent để mở ứng dụng khi nhấp vào thông báo
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ActivityMusicControl::class.java),
            PendingIntent.FLAG_MUTABLE
        )

        // Tạo Intent cho action (play hoặc pause)
        val actionIntent = if (player.isPlaying) {
            PendingIntent.getService(
                this,
                0,
                Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PAUSE },
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getService(
                this,
                0,
                Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PLAY },
                PendingIntent.FLAG_MUTABLE
            )
        }
        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_STOP},
            PendingIntent.FLAG_MUTABLE
        )
        val nextIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_NEXT},
            PendingIntent.FLAG_MUTABLE
        )

        // Xây dựng thông báo
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Your Audio Player")
            .setContentText("Title")
            .setSmallIcon(R.drawable.ic_alert_20_outlined)
            .setContentIntent(contentIntent)
            .addAction(
                if (player.isPlaying) R.drawable.ic_pause_20_filled else R.drawable.ic_play_20_filled,
                if (player.isPlaying) "Pause" else "Play",
                actionIntent
            )
            .addAction(R.drawable.ic_next_20_filled,"Next",nextIntent)
            .addAction(R.drawable.ic_cancel_16_outlined, "Stop", stopIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3) // Hiển thị các action play, pause, next, previous trong chế độ nhỏ gọn
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW) // Ưu tiên thấp để không làm phiền người dùng
        // Trả về thông báo đã tạo
        return notificationBuilder.build()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Your Audio Player Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification() {
        // Cập nhật nội dung thông báo (ví dụ: trạng thái play/pause)
        val notification = createNotification()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    // Lưu track index vào SharedPreferences
    fun saveTrackIndexToSharedPreferences(context: Context, trackIndex: Int) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("trackIndex", trackIndex)
        editor.apply()
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}