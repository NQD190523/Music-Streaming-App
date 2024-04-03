package com.project.appealic.data.repository

import com.project.appealic.data.model.Album
import com.project.appealic.data.network.SpotifyApiService
import com.project.appealic.data.network.SpotifyAuthClient
import okhttp3.Callback
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyRepository() {

    private val AUTH_BASE_URL = "https://api.spotify.com/v1/"

    private val httpClient = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(AUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
    val apiService = retrofit.create(SpotifyApiService::class.java)

    fun getAlbum(accessToken: String, albumId: String, callback: Callback) {
        apiService.getAlbum(accessToken, albumId).enqueue(callback)
    }


}