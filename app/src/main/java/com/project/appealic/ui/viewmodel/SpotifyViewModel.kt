package com.project.appealic.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.appealic.data.repository.SpotifyRepository

class SpotifyViewModel(private val repository: SpotifyRepository) : ViewModel() {
    val spotifyConnected : LiveData<Boolean>  =  repository.spotifyConnected

    constructor() : this(SpotifyRepository())
    fun connectToSpotify(context: Context){
        repository.connectToSpotify(context)
    }

    fun disconnectFromSpotify(){
        repository.disconnectFromSpotify()
    }
}