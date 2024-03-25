package com.project.appealic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.appealic.data.network.SpotifyAuthClient
import okhttp3.Callback

import java.io.IOException

class SpotifyAuthViewModel : ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String>
        get() = _accessToken

    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String>
        get() = _authError
    fun requestAccessToken() {
        SpotifyAuthClient.requestAccessToken( object : Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val token = response.body?.string()
                    if (token != null) {
                        println("access token : $token")
                    }
                    _accessToken.postValue(token)
                } else {
                    // Handle error response
                    _authError.postValue("Failed to get access token")
                }
            }
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // Handle failure
                _authError.postValue("Network error occurred")
            }

        })
    }
}