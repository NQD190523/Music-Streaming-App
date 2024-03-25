package com.project.appealic.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.ui.view.MediaActivity

class PlayerViewModel : ViewModel() {
    var player : ExoPlayer? = null

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    fun initializePlayer(context: Context) {
        if (player == null) {
            player = ExoPlayer.Builder(context).build()
        }
    }

    fun prepareMedia(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
    }
    fun playMedia(){
        player?.playWhenReady = true
        _isPlaying.value = true
    }
    fun stopMedia(){
        player?.stop()
    }
    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}