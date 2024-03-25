package com.project.appealic.data.network

import com.project.appealic.data.model.AccessTokenRespone
import com.project.appealic.data.model.Track
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyApiService {
    @POST("token")
    fun getAccessToken(
        @Body requestBody: RequestBody
    ): Call<AccessTokenRespone>

    fun searchTrack(
        @Header("Authorization") token :String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ) : Call<Track>

    // Các phương thức khác cho các yêu cầu API khác của Spotify có thể được thêm vào đây
}