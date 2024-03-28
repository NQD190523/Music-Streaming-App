package com.project.appealic.data.network

import com.project.appealic.data.model.AccessTokenRespone
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Track
import okhttp3.Call
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiService {
    @POST("token")
    fun getAccessToken(
        @Body requestBody: RequestBody
    ): Call

    fun searchTrack(
        @Header("Authorization") token :String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ) : Call
    fun getAlbum(
        @Header("Authorization") token :String,
        @Path("album_id") albumId : String) : Call

    // Các phương thức khác cho các yêu cầu API khác của Spotify có thể được thêm vào đây
}