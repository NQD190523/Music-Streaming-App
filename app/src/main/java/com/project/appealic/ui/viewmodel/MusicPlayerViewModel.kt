package com.project.appealic.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.utils.MusicPlayerFactory

class MusicPlayerViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val currentPositionLiveData = MutableLiveData<Long>()
    private val player: ExoPlayer = MusicPlayerFactory.createSimpleExoPlayer(getApplication<Application>().applicationContext)
    private val handler = Handler(Looper.getMainLooper())

    private val AUDIO_POSITIONS_KEY = "audio_positions"
    private val audioPositionsLiveData: MutableLiveData<MutableMap<String, Long>> by lazy {
        savedStateHandle.getLiveData(AUDIO_POSITIONS_KEY, mutableMapOf())
    }
    private val audioPositionsMap: MutableMap<String, Long>
        get() = audioPositionsLiveData.value ?: mutableMapOf()

    init {
        player.addListener(object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
                // Cập nhật trạng thái phát nhạc vào LiveData
                currentPositionLiveData.postValue(player.currentPosition)

            }
        })
    }
    fun saveAudioPosition(audioId: String, position: Long) {
        audioPositionsMap[audioId] = position
        audioPositionsLiveData.value = audioPositionsMap
    }
    fun getAudioPosition(audioId: String): Long? {
        return audioPositionsMap[audioId]
    }
    fun getPlayerInstance(): ExoPlayer {
        return player
    }
    fun startPlaying(songUri: Uri) {
        val mediaItem = MediaItem.fromUri(songUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun stopPlaying() {
        player.stop()
        handler.removeCallbacksAndMessages(null)

    }

    fun getCurrentPositionLiveData(): LiveData<Long> {
        return currentPositionLiveData
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun onSaveInstanceState(): Bundle {
        val bundle = Bundle()
        // Lưu trạng thái của ExoPlayer
        bundle.putLong("currentPosition", player.currentPosition)
        return bundle
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        // Khôi phục trạng thái của ExoPlayer
        val currentPosition = bundle.getLong("currentPosition", 0)
        currentPositionLiveData.postValue(currentPosition)
        player.seekTo(currentPosition)
    }
    fun observeCurrentPosition(observer: Observer<Long>) {
        handler.post(object : Runnable {
            override fun run() {
                observer.onChanged(player.currentPosition)
                handler.postDelayed(this, 1000) // Cập nhật mỗi giây
            }
        })
    }


}