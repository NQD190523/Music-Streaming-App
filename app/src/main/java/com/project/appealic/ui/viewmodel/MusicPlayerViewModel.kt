package com.project.appealic.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.service.MusicPlayerService
import kotlinx.coroutines.launch

class MusicPlayerViewModel :ViewModel() {

    private val _currentSong = MutableLiveData<Track>()
    val currentSong: LiveData<Track> = _currentSong

    private var musicPlayerService: MusicPlayerService? = null
    val currentPosition = MutableLiveData<Long>()
    private val _serviceReady = MutableLiveData<Boolean>()
    val serviceReady: LiveData<Boolean> = _serviceReady
    fun setMusicService(service: MusicPlayerService) {
        musicPlayerService = service
        _serviceReady.value = true
        musicPlayerService?.getCurrentPositionLiveData()?.observeForever { current ->
            currentPosition.value = current
        }
    }
    fun setMediaUri(uri: MutableList<MediaItem>, startIndex : Int) {
        viewModelScope.launch {
            musicPlayerService?.setMediaUri(uri, startIndex)
        }
    }

    fun setCurrentSong(song: Track) {
        _currentSong.value = song
    }
    fun play() {
        musicPlayerService?.play()
    }
    fun pause() {
        musicPlayerService?.pause()
    }
    fun getPlayerInstance(): ExoPlayer? {
        return musicPlayerService?.getExoPlayerInstance()
    }
    fun getCurrentPositionLiveData(): LiveData<Long> {
        return currentPosition
    }
    fun nextButtonClick() {
        musicPlayerService?.nextButtonClick()
    }
    fun previousButtonClick() {
        musicPlayerService?.previousButtonClick()
    }
    fun repeatButtonClick(){
        musicPlayerService?.repeatButtonClick()
    }
    fun handleListViewItemClick(intent: Intent ,parent: AdapterView<*>, songViewModel: SongViewModel, context : Context, position: Int) {
        val selectedSong = parent.getItemAtPosition(position) as Track
        val user = FirebaseAuth.getInstance().currentUser?.uid
        val trackUrlList = ArrayList<String>()

        val song = selectedSong.trackId?.let {
            SongEntity(
                it,
                selectedSong.trackImage,
                selectedSong.trackTitle,
                selectedSong.artist,
                user,
                null,
                System.currentTimeMillis(),
                null,
                selectedSong.duration?.toLong(),
                selectedSong.artistId,
            )
        }

        if (song != null) {
            songViewModel.insertSong(song)
            Log.d(" test status", "success")
        }
        // Lấy dữ liệu các url trong playlist
        for (i in 0 until parent.count) {
            val item = parent.getItemAtPosition(i) as Track
            item.trackUrl?.let { trackUrl ->
                println(trackUrl)
                trackUrlList.add(trackUrl)
            }
        }

        // Truyền dữ liệu cần thiết qua Intent
        intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
        intent.putExtra("SINGER_NAME", selectedSong.artist)
        intent.putExtra("SONG_NAME", selectedSong.trackTitle)
        intent.putExtra("TRACK_IMAGE", selectedSong.trackImage)
        intent.putExtra("ARTIST_ID", selectedSong.artistId)
        intent.putExtra("DURATION", selectedSong.duration)
        intent.putExtra("TRACK_URL", selectedSong.trackUrl)
        intent.putExtra("TRACK_ID", selectedSong.trackId)
        intent.putExtra("TRACK_INDEX", position)
        intent.putStringArrayListExtra("TRACK_LIST", trackUrlList)
    }



//    fun saveAudioPosition(trackId: String, position: Long) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val song = Song(trackId, position)
//            songDao.insertSong(song)
//        }
//    }


}