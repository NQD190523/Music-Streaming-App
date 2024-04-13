package com.project.appealic.data.repository.service

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.R
import com.project.appealic.ui.view.ActivityPlaylist
import com.project.appealic.utils.MusicPlayerFactory
import javax.sql.DataSource

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
    companion object {
        private const val NOTIFICATION_ID = 1
        const val ACTION_PLAY = "com.project.appealic.action.PLAY"
        const val ACTION_PAUSE = "com.project.appealic.action.action.PAUSE"
        const val ACTION_STOP = "com.project.appealic.action.action.STOP"
        private const val CHANNEL_ID = "123"
    }


    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        trackCurrentPosition()

        // Khởi tạo NotificationManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Tạo kênh thông báo
        createNotificationChannel()
        // Bắt đầu dịch vụ
        startForeground(NOTIFICATION_ID,createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY.toString() -> {
                // Xử lý yêu cầu play
                player.play()
                updateNotification()
            }
            ACTION_PAUSE.toString() -> {
                // Xử lý yêu cầu pause
                player.pause()
                updateNotification()
            }
            ACTION_STOP.toString() -> {
                // Xử lý yêu cầu stop
                stopSelf() // Dừng service khi người dùng nhấn stop
            }
        }
        return START_NOT_STICKY
    }

    fun setMediaUri(uri: MutableList<MediaItem>, startIndex : Int) {
        player.setMediaItems(uri)
        player.prepare()
        player.seekToDefaultPosition(startIndex)
        player.play()
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
            println(currentMediaItemIndex)
            println(player.mediaItemCount)
            // Xác định vị trí của mục phương tiện kế tiếp
            val nextMediaItemIndex = if (currentMediaItemIndex < player.mediaItemCount - 1) currentMediaItemIndex + 1 else currentMediaItemIndex
            // Chuyển đến mục phương tiện kế tiếp
            player.seekToDefaultPosition(nextMediaItemIndex)
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
            Intent(this, ActivityPlaylist::class.java),
            PendingIntent.FLAG_MUTABLE
        )

        // Tạo các action cho thông báo (play, pause, stop)
        val playIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PLAY.toString() },
            PendingIntent.FLAG_MUTABLE
        )

        val pauseIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PAUSE.toString() },
            PendingIntent.FLAG_MUTABLE
        )

        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_STOP.toString() },
            PendingIntent.FLAG_MUTABLE
        )

        // Xây dựng thông báo
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Your Audio Player")
            .setContentText("Now playing...")
            .setSmallIcon(R.drawable.ic_alert_20_outlined)
            .setContentIntent(contentIntent)
            .addAction(R.drawable.ic_play_20_filled, "Play", playIntent)
            .addAction(R.drawable.ic_pause_20_filled, "Pause", pauseIntent)
            .addAction(R.drawable.ic_cancel_16_outlined, "Stop", stopIntent)

        // Trả về thông báo đã tạo
        return notificationBuilder.build()
    }

    private fun updateNotification() {
        // Cập nhật nội dung thông báo (ví dụ: trạng thái play/pause)
        val notification = createNotification()
        notificationManager.notify(NOTIFICATION_ID, notification)
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

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}