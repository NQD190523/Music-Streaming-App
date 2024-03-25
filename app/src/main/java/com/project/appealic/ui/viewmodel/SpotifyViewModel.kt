package com.project.appealic.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.appealic.data.model.Track
import com.project.appealic.data.network.SpotifyApiService
import com.project.appealic.data.repository.SpotifyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.RequestBody

class SpotifyViewModel(private val spotifyApiService: SpotifyApiService) : ViewModel() {

    fun searchTrack(token : String ,query : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = spotifyApiService.searchTrack(token, query,"single",1)
            } catch (e :Exception) {
                println(e)
            }
        }
    }

//    fun connectToSpotify(context: Context){
//        repository.connectToSpotify(context)
//    }
//
//    fun disconnectFromSpotify(){
//        repository.disconnectFromSpotify()
//    }
}

