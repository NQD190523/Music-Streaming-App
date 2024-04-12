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

    private val _currentPosition = MutableLiveData<Long>()
    val currentPosition: LiveData<Long> = _currentPosition

    private var musicPlayerService: MusicPlayerService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicPlayerService.MusicBinder
            musicPlayerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    fun bindService(context: Context) {
        val intent = Intent(context, MusicPlayerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(context: Context) {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
    }

    // Phương thức để bắt đầu phát nhạc từ ViewModel

//    fun getExoPlayerInstance(): ExoPlayer {
//        return musicPlayerService?.getExoPlayerInstance() ?:
//    }
    fun startPlaying(songUri: Uri) {
        // Sử dụng MusicPlayerService để bắt đầu phát nhạc từ Uri
        musicPlayerService?.initializePlayer(songUri)
    }
    fun playMusic() {
        musicPlayerService?.playMusic()
    }

    // Phương thức để tạm dừng phát nhạc từ ViewModel
    fun pauseMusic() {
        musicPlayerService?.pauseMusic()
    }
    fun stopMusic(){
        musicPlayerService?.stopPlayer()
    }

    // Phương thức để chuyển bài hát tiếp theo từ ViewModel
    fun skipToNext() {
        musicPlayerService?.skipToNext()
    }

//    fun saveAudioPosition(trackId: String, position: Long) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val song = Song(trackId, position)
//            songDao.insertSong(song)
//        }
//    }


}