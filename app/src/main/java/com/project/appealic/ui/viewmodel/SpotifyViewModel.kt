package com.project.appealic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.project.appealic.data.model.AccessTokenRespone
import com.project.appealic.data.model.Album
import com.project.appealic.data.network.SpotifyAuthClient
import com.project.appealic.data.repository.SpotifyRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class SpotifyViewModel(private val spotifyRepository: SpotifyRepository) : ViewModel() {

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String>
        get() = _accessToken

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album>
        get() = _album


    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String>
        get() = _authError


    fun getAlbum(accessToken : String, albumId : String) {
        spotifyRepository.getAlbum(accessToken,albumId, object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                _authError.postValue("Failed to get album")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val album = response.body?.string()
                    val albumProject = Gson().fromJson(album,Album::class.java)
                    _album.postValue(albumProject)
                }
            }

        })
    }

}

