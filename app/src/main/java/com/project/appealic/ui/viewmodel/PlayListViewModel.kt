package com.project.appealic.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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

    private val firebaseDB = Firebase.firestore

    private val _userPlayLists = MutableLiveData<List<PlayListEntity>>()
    val userPlayLists: LiveData<List<PlayListEntity>> get() = _userPlayLists

    private val _playLists = MutableLiveData<List<Playlist>>()
    val playLists: LiveData<List<Playlist>> get() = _playLists

    private val _tracks = MutableLiveData<List<Track>>()
    val track: LiveData<List<Track>> get() = _tracks

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

    fun getTracksFromPlaylist(playlistId : String) {
        playListRepository.getTracksFromPlaylist(playlistId)
            .addOnSuccessListener { tracks ->
                if (tracks != null){
                    val trackIds = tracks["trackIds"] as? List<String> ?: emptyList()

                    val tasks = trackIds.map { trackId ->
                        val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result?.toObject(Track::class.java)
                            } else {
                                null
                            }
                        }
                    }
                    Tasks.whenAllSuccess<Track?>(tasks).addOnSuccessListener { documents ->
                        val tracksList = documents.filterNotNull()
                        // Xử lý danh sách trackList ở đây
                        _tracks.postValue(tracksList)
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
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
    fun SearchPlaylistResults(searchQuery: String?) {
        // Gọi phương thức trong Repository để tải dữ liệu từ Firebase dựa trên searchQuery
        val searchResultsLiveData = playListRepository.loadPlaylistSearchResults(searchQuery)
        // Cập nhật LiveData _tracks với dữ liệu mới
        searchResultsLiveData.observeForever { playlists ->
            _playLists.postValue(playlists) }
    }
}
