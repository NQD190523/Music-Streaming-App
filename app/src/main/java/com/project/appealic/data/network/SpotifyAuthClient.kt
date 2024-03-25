package com.project.appealic.data.network

import com.project.appealic.data.model.AccessTokenRespone
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyAuthClient {


    private const val CLIENT_ID = "628d7590b18c4102919b1ec0da29ae83"
    private const val CLIENT_SECRET = "abc242a53f37464dbb362abea9ae2131"
    private const val AUTH_BASE_URL = "https://accounts.spotify.com/"
    private const val TOKEN_PATH = "api/token"

    private val httpClient = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(AUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
    private val apiService = retrofit.create(SpotifyApiService::class.java)


    fun requestAccessToken( callback: Callback){
        val requestBody = FormBody.Builder()
            .add("grant_type","client_credentials")
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .build()
        val request = Request.Builder()
            .url("$AUTH_BASE_URL$TOKEN_PATH")
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(callback)
    }
}