package com.project.appealic.utils

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayerFactory {
    companion object {
        fun createSimpleExoPlayer(context: Context): ExoPlayer {
            return ExoPlayer.Builder(context).build()
        }
    }
}