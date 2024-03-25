package com.project.appealic.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.appealic.data.model.AccessTokenRespone
import com.project.appealic.data.network.SpotifyApiService
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import okhttp3.RequestBody

class SpotifyRepository(private val apiService: SpotifyApiService) {
    private val _spotifyConnected = MutableLiveData<Boolean>()
    val spotifyConnected : LiveData<Boolean> = _spotifyConnected

    private val BASE_URL = "https://api.spotify.com"
    private val CLIENT_ID = "628d7590b18c4102919b1ec0da29ae83"
    private val CLIENT_SECRET = "abc242a53f37464dbb362abea9ae2131"

    suspend fun getAccessToken(requestBody: RequestBody): String? {
        return try {
            // Gửi yêu cầu để nhận access token từ Spotify API
            val response = apiService.getAccessToken(requestBody).execute()

            // Kiểm tra xem yêu cầu có thành công không
            if (response.isSuccessful) {
                // Chuyển đổi phản hồi thành đối tượng AccessTokenResponse
                val accessTokenResponse = response.body()

                // Trả về access token từ phản hồi
                accessTokenResponse?.accessToken
            } else {
                // Nếu không thành công, trả về null
                null
            }
        } catch (e: Exception) {
            // Nếu có lỗi xảy ra, trả về null
            null
        }
    }


//    suspend fun authenticate(): AccessTokenRespone? {
//        return try {
//            SpotifyApiClient.authenticate()
//        } catch (e: Exception) {
//            null
//        }
//    }


//    fun connectToSpotify(context: Context){
//        val connectionParams = ConnectionParams.Builder(clientId)
//            .setRedirectUri(redirectUri)
//            .showAuthView(true)
//            .build()
//
//        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
//            override fun onConnected(appRemote: SpotifyAppRemote) {
//                spotifyAppRemote = appRemote
//                _spotifyConnected.value = true
//                Log.d("MainActivity", "Connected! Yay!")
//                connected()
//                // Now you can start interacting with App Remote
//            }
//
//            override fun onFailure(p0: Throwable) {
//                Log.e("error",p0.message,p0)
//            }
//
//        })
//    }
//    private fun connected() {
//        spotifyAppRemote?.let {
//            // Play a playlist
//            val playlistURI = "spotify:track:1VUGWjzlqudhTqvhc17miB"
//            it.playerApi.play(playlistURI)
//            // Subscribe to PlayerState
//            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("MainActivity", track.name + " by " + track.artist.name)
//            }
//        }
//    }
//    fun disconnectFromSpotify() {
//        spotifyAppRemote?.let {
//            SpotifyAppRemote.disconnect(it)
//        }
//    }

}