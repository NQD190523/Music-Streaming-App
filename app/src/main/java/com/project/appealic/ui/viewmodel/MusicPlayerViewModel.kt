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
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.utils.MusicPlayerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicPlayerViewModel :ViewModel() {

    private val _currentSong = MutableLiveData<Track>()
    val currentSong: LiveData<Track> = _currentSong


    private var musicPlayerService: MusicPlayerService? = null
    val currentPosition = MutableLiveData<Long>()
    private val _serviceReady = MutableLiveData<Boolean>()
    val serviceReady: LiveData<Boolean> = _serviceReady
    fun setMusicService(service: MusicPlayerService) {
        musicPlayerService = service
        _serviceReady.value = true
        musicPlayerService?.getCurrentPositionLiveData()?.observeForever { current ->
            currentPosition.value = current
        }
    }
    fun setMediaUri(uri: MutableList<MediaItem>, startIndex : Int) {
        viewModelScope.launch {
            musicPlayerService?.setMediaUri(uri, startIndex)
        }
    }

    fun setCurrentSong(song: Track) {
        _currentSong.value = song
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
        return currentPosition
    }
    fun nextButtonClick() {
        musicPlayerService?.nextButtonClick()
    }
    fun previousButtonClick() {
        musicPlayerService?.previousButtonClick()
    }
    fun repeatButtonClick(){
        musicPlayerService?.repeatButtonClick()
    }



//    fun saveAudioPosition(trackId: String, position: Long) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val song = Song(trackId, position)
//            songDao.insertSong(song)
//        }
//    }


}