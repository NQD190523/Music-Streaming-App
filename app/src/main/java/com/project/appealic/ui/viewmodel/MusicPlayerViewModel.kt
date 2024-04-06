package com.project.appealic.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.project.appealic.utils.MusicPlayerUtils

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {
    val exoPlayer : ExoPlayer = MusicPlayerUtils.getInstance(application)

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}