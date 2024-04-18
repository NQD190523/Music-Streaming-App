package com.project.appealic.ui.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.UserPlaylist
import com.project.appealic.data.model.UserWithPlayLists
import com.project.appealic.data.repository.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PlayListViewModel(private val playListRepository: PlayListRepository) :ViewModel() {
    private val _userPlayLists = MutableLiveData<List<PlayListEntity>>()
    val userPlayLists: LiveData<List<PlayListEntity>> get() = _userPlayLists

    private val _playLists = MutableLiveData<List<Playlist>>()
    val playLists: LiveData<List<Playlist>> get() = _playLists
    fun createNewPlayList(playList: PlayListEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playListRepository.createNewPlayList(playList)
        }
    }

    fun getUserPlaylist(uid: String) = viewModelScope.launch {
        try {
            val playListsLiveData = withContext(Dispatchers.IO) {
                playListRepository.getAllUserPlayList(uid)
            }
            // Convert LiveData to Flow and collect the first value
            playListsLiveData.asFlow().first().let { playLists ->
                _userPlayLists.postValue(playLists)
            }
        } catch (e: Exception) {
            Log.e("PlayListViewModel", "Error fetching user playlists", e)
            // Handle the error, e.g., show a message to the user
        }
    }

    fun getAllPlaylists() {
        playListRepository.getAllPlaylists()
            .addOnSuccessListener { playlist ->
                if (playlist != null) _playLists.postValue(playlist.toObjects(Playlist::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }



    fun addTrackToPlaylist(track: Track, playlist: PlayListEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playListRepository.addTrackToPlaylist(track, playlist)
            }
            Log.d("PlayListViewModel", "Track added to playlist: ${playlist.playListName}")

        }
    }
    }
