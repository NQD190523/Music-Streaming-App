package com.project.appealic.data.repository.service

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.media.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
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


    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        trackCurrentPosition()
        // Khởi tạo NotificationManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNo
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
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Tạo các action cho thông báo (play, pause, stop)
        val playIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PLAY.toString() },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pauseIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_PAUSE.toString() },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicPlayerService::class.java).apply { action = ACTION_STOP.toString() },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Xây dựng thông báo
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Your Audio Player")
            .setContentText("Now playing...")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(contentIntent)
            .addAction(R.drawable.ic_play, "Play", playIntent)
            .addAction(R.drawable.ic_pause, "Pause", pauseIntent)
            .addAction(R.drawable.ic_stop, "Stop", stopIntent)

        // Trả về thông báo đã tạo
        return notificationBuilder.build()
    }

    private fun updateNotification() {
        // Cập nhật nội dung thông báo (ví dụ: trạng thái play/pause)
        val notification = createNotification()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
    //    private val currentPositionLiveData = MutableLiveData<Long>()
//    private val player: ExoPlayer = MusicPlayerFactory.createSimpleExoPlayer(getApplication<Application>().applicationContext)
//    private val handler = Handler(Looper.getMainLooper())
//
//    private val AUDIO_POSITIONS_KEY = "audio_positions"
//    private val audioPositionsLiveData: MutableLiveData<MutableMap<String, Long>> by lazy {
//        savedStateHandle.getLiveData(AUDIO_POSITIONS_KEY, mutableMapOf())
//    }
//    private val audioPositionsMap: MutableMap<String, Long>
//        get() = audioPositionsLiveData.value ?: mutableMapOf()
//
//    init {
//        player.addListener(object : Player.Listener {
//            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
//                super.onTimelineChanged(timeline, reason)
//                // Cập nhật trạng thái phát nhạc vào LiveData
//                currentPositionLiveData.postValue(player.currentPosition)
//
//            }
//        })
//    }
//    fun saveAudioPosition(audioId: String, position: Long) {
//        audioPositionsMap[audioId] = position
//        audioPositionsLiveData.value = audioPositionsMap
//    }
//    fun getAudioPosition(audioId: String): Long? {
//        return audioPositionsMap[audioId]
//    }
//    fun getPlayerInstance(): ExoPlayer {
//        return player
//    }
//    fun startPlaying(songUri: Uri) {
//        val mediaItem = MediaItem.fromUri(songUri)
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()
//    }
//
//    fun stopPlaying() {
//        player.stop()
//        handler.removeCallbacksAndMessages(null)
//
//    }
//
//    fun getCurrentPositionLiveData(): LiveData<Long> {
//        return currentPositionLiveData
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        player.release()
//    }
//
//    fun onSaveInstanceState(): Bundle {
//        val bundle = Bundle()
//        // Lưu trạng thái của ExoPlayer
//        bundle.putLong("currentPosition", player.currentPosition)
//        return bundle
//    }
//
//    fun onRestoreInstanceState(bundle: Bundle) {
//        // Khôi phục trạng thái của ExoPlayer
//        val currentPosition = bundle.getLong("currentPosition", 0)
//        currentPositionLiveData.postValue(currentPosition)
//        player.seekTo(currentPosition)
//    }
//    fun observeCurrentPosition(observer: Observer<Long>) {
//        handler.post(object : Runnable {
//            override fun run() {
//                observer.onChanged(player.currentPosition)
//                handler.postDelayed(this, 1000) // Cập nhật mỗi giây
//            }
//        })
//    }
}