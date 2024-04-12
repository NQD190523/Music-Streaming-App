package com.project.appealic.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.utils.MusicPlayerFactory

class MusicPlayerViewModel(private val musicPlayerService: MusicPlayerService) :ViewModel() {

    private val _currentPosition = MutableLiveData<Long>()
    val currentPosition: LiveData<Long> = _currentPosition

    init {
        musicPlayerService.observeCurrentPosition().observeForever {
            _currentPosition.value = it
        }
    }
    // Phương thức để bắt đầu phát nhạc từ ViewModel

    fun getExoPlayerInstance(): ExoPlayer {
        return musicPlayerService.getExoPlayerInstance()
    }
    fun startPlaying(songUri: Uri) {
        // Sử dụng MusicPlayerService để bắt đầu phát nhạc từ Uri
        musicPlayerService.initializePlayer(songUri)
    }
    fun playMusic() {
        musicPlayerService.playMusic()
    }

    // Phương thức để tạm dừng phát nhạc từ ViewModel
    fun pauseMusic() {
        musicPlayerService.pauseMusic()
    }
    fun stopMusic(){
        musicPlayerService.stopPlayer()
    }

    // Phương thức để chuyển bài hát tiếp theo từ ViewModel
    fun skipToNext() {
        musicPlayerService.skipToNext()
    }

//    fun saveAudioPosition(trackId: String, position: Long) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val song = Song(trackId, position)
//            songDao.insertSong(song)
//        }
//    }


}