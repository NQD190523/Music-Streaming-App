package com.project.appealic.utils

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

object MusicPlayerUtils {
    private var exoPlayer: ExoPlayer? = null

    fun getInstance(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer!!
    }
}