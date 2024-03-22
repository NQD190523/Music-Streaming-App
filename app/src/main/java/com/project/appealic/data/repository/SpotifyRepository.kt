package com.project.appealic.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class SpotifyRepository {
    private val _spotifyConnected = MutableLiveData<Boolean>()
    val spotifyConnected : LiveData<Boolean> = _spotifyConnected

    private val clientId = "628d7590b18c4102919b1ec0da29ae83"
    private val redirectUri = "http://localhost:8888/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null


    fun connectToSpotify(context: Context){
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                _spotifyConnected.value = true
                Log.d("MainActivity", "Connected! Yay!")
                connected()
                // Now you can start interacting with App Remote
            }

            override fun onFailure(p0: Throwable) {
                Log.e("error",p0.message,p0)
            }

        })
    }
    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:track:1VUGWjzlqudhTqvhc17miB"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
            }
        }
    }
    fun disconnectFromSpotify() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

}