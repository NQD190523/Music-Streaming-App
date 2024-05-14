package com.project.appealic.ui.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.google.firebase.firestore.toObject
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

class PlayListViewModel(private val playListRepository: PlayListRepository) : ViewModel() {

    private val firebaseDB = Firebase.firestore

    private val _userPlayLists = MutableLiveData<List<PlayListEntity>?>()
    val userPlayLists: MutableLiveData<List<PlayListEntity>?> get() = _userPlayLists

    private val _playLists = MutableLiveData<List<Playlist>>()
    val playLists: LiveData<List<Playlist>> get() = _playLists

    private val _tracks = MutableLiveData<List<Track>>()
    val track: LiveData<List<Track>> get() = _tracks

    fun createNewPlayList(playList: PlayListEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playListRepository.createNewPlaylist(playList)
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
    fun getTracksFromUserPlaylist(uid: String,  playlistId: Int) = viewModelScope.launch{
        withContext(Dispatchers.IO){
            val playList = playListRepository.getAllUserPlayList(uid)
            val trackIds = playList[playlistId].trackIds as? List<String> ?: emptyList()
            if (trackIds.isNotEmpty()) {
                val tasks = trackIds.mapNotNull { trackId ->
                    // Kiểm tra trackId có tồn tại không trước khi tạo tham chiếu tài liệu
                    if (trackId.isNotBlank()) {
                        val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result.toObject(Track::class.java)
                            } else {
                                null
                            }
                        }
                    } else {
                        null
                    }
                }
                Tasks.whenAllComplete(tasks).addOnCompleteListener { document ->
                    val trackList = document.result?.filter { it.isSuccessful }?.mapNotNull { it.result as? Track } ?: emptyList()
                    _tracks.postValue(trackList)
                }.addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                }
            } else {
                // Xử lý trường hợp không có tracks để thực hiện
                Log.d(TAG, "No tracks to fetch")
                _tracks.postValue(emptyList()) // Đặt giá trị của tracks là danh sách rỗng
            }
        }
    }

    fun getTracksFromPlaylist(playlistId: String) {
        playListRepository.getTracksFromPlaylist(playlistId)
            .addOnSuccessListener { tracks ->
                if (tracks != null) {
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
                        _tracks.postValue(tracksList)
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
            }
    }

    fun getUserPlaylist(uid: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                val playList = playListRepository.getAllUserPlayList(uid)
                _userPlayLists.postValue(playList)
            } catch (e: Exception) {
                Log.e("PlayListViewModel", "Error fetching user playlists", e)
            }
        }
    }

    fun SearchPlaylistResults(searchQuery: String?) {
        val searchResultsLiveData = playListRepository.loadPlaylistSearchResults(searchQuery)
        searchResultsLiveData.observeForever { playlists ->
            _playLists.postValue(playlists)
        }
    }

    fun addTrackToPlaylist( playlist: PlayListEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                playListRepository.addTrackToPlaylist( playlist)
            } catch (e: Exception){
                Log.e("PlayListViewModel", "Error add track to playlist", e)
            }
        }
    }
}