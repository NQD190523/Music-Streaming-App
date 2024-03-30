package com.project.appealic.ui.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.project.appealic.R
import com.project.appealic.data.model.AccessTokenRespone
import com.project.appealic.data.model.ExternalUrlsXXX
import com.project.appealic.data.model.Track
import com.project.appealic.data.network.RetrofitClient
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.databinding.ActivityPlaysongBinding
import com.project.appealic.ui.viewmodel.PlayerViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.ui.viewmodel.SongViewModelFactory
import com.project.appealic.ui.viewmodel.SpotifyAuthViewModel
import com.project.appealic.ui.viewmodel.SpotifyViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MediaActivity: AppCompatActivity() {

    private lateinit var binding : ActivityPlaysongBinding

    private lateinit var player : ExoPlayer
    private  val viewModel: PlayerViewModel by viewModels()
    private lateinit var spotifyAuthViewModel: SpotifyAuthViewModel
    private lateinit var spotifyViewModel: SpotifyViewModel

    private lateinit var  songViewModel : SongViewModel

    private var songRepository: SongRepository = SongRepository()

    val apiService = RetrofitClient.createSpotifyApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaysongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = SongViewModelFactory(songRepository )
        songViewModel = ViewModelProvider(this,factory).get(SongViewModel::class.java)
        songViewModel.getAllTracks()
        songViewModel.tracks.observe(this, Observer { tracks ->
            Log.d("info", tracks.toString())
        })



//        spotifyAuthViewModel= ViewModelProvider(this).get(SpotifyAuthViewModel::class.java)
////        spotifyViewModel = ViewModelProvider(this).get(SpotifyViewModel::class.java)
//
//
//        spotifyAuthViewModel.requestAccessToken()
//        spotifyAuthViewModel.accessToken.observe(this, Observer {
//            token->
//                val tokenObject = Gson().fromJson(token,AccessTokenRespone::class.java)
//                val accessToken = tokenObject.accessToken
//                val client = OkHttpClient.Builder().build()
//
//            // Tạo yêu cầu GET để lấy thông tin từ API của Spotify
//            val request = Request.Builder()
//                .url("https://api.spotify.com/v1/tracks/2nzED6Q3yWuvSQnxw6QTlU") // URL của API bạn muốn truy vấn
//                .header("Authorization", "Bearer $accessToken") // Thêm access token vào tiêu đề Authorization
//                .build()
//
//            // Gửi yêu cầu bằng cách sử dụng OkHttpClient
//            client.newCall(request).enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body?.string()
//                        println("Response: $responseBody")
//                        val spotifyTrackUrl = "https://api.spotify.com/v1/tracks/11dFghVXANMlKmJXsNCbNl/audio"
//                        val source = Gson().fromJson(responseBody, Track::class.java)
//                    } else {
//                        println("Request failed with code: ${response.code}")
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    println("Request failed: ${e.message}")
//                }
//            })
//        })
//        val storageReference = FirebaseStorage.getInstance().reference.child("gs://music-streaming-app-f668e.appspot.com/HayTraoChoAnh-SonTungMTPSnoopDogg-6010660.mp3")
//        storageReference.downloadUrl.addOnSuccessListener { uri ->
//            // Lấy URL công khai thành công
//            val audioUrl = uri.toString()
//         // Sử dụng audioUrl để phát lại audio bằng ExoPlayer hoặc thư viện phát âm thanh khác
//        }.addOnFailureListener { exception ->
//            // Xử lý khi không thể lấy URL của audio
//        }
//
//        viewModel.initializePlayer(this)
//        player = viewModel.player!!
//
//        binding.play.setOnClickListener(View.OnClickListener {
//            if (player.isPlaying) {
//                player.pause()
//                binding.play.setImageResource(R.drawable.button_color_white) // Sử dụng biểu tượng play
//            } else {
//                player.play()
//                binding.play.setImageResource(R.drawable.ic_play_20_outlined) // Sử dụng biểu tượng pause
//            }
//        })


    }
}