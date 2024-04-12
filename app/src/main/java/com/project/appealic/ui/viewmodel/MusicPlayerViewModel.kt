package com.project.appealic.ui.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.utils.MusicPlayerFactory

class MusicPlayerViewModel :ViewModel() {
    private var musicPlayerService: MusicPlayerService? = null
    val currentPosition = MutableLiveData<Long>()

    fun setMusicService(service: MusicPlayerService) {
        musicPlayerService = service
        musicPlayerService.getCurrentPositionLiveData().observeForever { currentPosition ->
            currentPositionLiveData.value = currentPosition
        }
    }
    fun setMediaUri(uri: Uri) {
        musicPlayerService?.setMediaUri(uri)
    }
    fun play() {
        musicPlayerService?.play()
    }
    fun pause() {
        musicPlayerService?.pause()
    }
    fun getPlayerInstance(): ExoPlayer? {
        return musicPlayerService?.getExoPlayerInstance()
    }
    fun getCurrentPositionLiveData(): LiveData<Long> {
        return currentPositionLiveData
    }


//    fun saveAudioPosition(trackId: String, position: Long) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val song = Song(trackId, position)
//            songDao.insertSong(song)
//        }
//    }


}