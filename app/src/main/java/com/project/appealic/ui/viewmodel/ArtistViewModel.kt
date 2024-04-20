package com.project.appealic.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel(private val artistRepository: ArtistRepository): ViewModel() {

    private val firebaseDB = Firebase.firestore
    private val _likedArtist = MutableLiveData<List<Artist>>()
    val likedArtist: LiveData<List<Artist>> get() = _likedArtist

    private val _tracks = MutableLiveData<List<Track>>()
    val track: LiveData<List<Track>> get() = _tracks
    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    fun getTracksFromArtist(artistId: String){
        artistRepository.getTrackFromArtist(artistId)
            .addOnSuccessListener { artist->
                val artistDocument = artist.documents.firstOrNull()
                if (artistDocument != null) {
                    val trackIds = artistDocument["trackIds"] as? List<String>

                    if (!trackIds.isNullOrEmpty()) {
                        val tasks = trackIds.map { trackId ->
                            val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                            trackDocRef.get().continueWith { task ->
                                if (task.isSuccessful) {
                                    task.result?.toObject<Track>()
                                } else {
                                    null
                                }
                            }
                        }
                        Tasks.whenAllSuccess<Track?>(tasks).addOnSuccessListener { documents ->
                            val tracksList = documents.filterNotNull()
                            // Gán danh sách các bài hát vào LiveData để cập nhật giao diện
                            _tracks.postValue(tracksList)
                        }.addOnFailureListener { exception ->
                            // Xử lý khi có lỗi xảy ra
                            Log.e("Error", exception.toString())
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                // Xử lý khi có lỗi xảy ra
                Log.e("Error", exception.toString())
            }
    }

    fun getFollowArtistFromUser(userId: String){
        artistRepository.getFollowArtistFromUser(userId)
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val favoriteSongIds = userDoc["followArtist"] as? List<String> ?: emptyList()

                    val tasks = favoriteSongIds.map { trackId ->
                        val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result?.toObject(Artist::class.java)
                            } else {
                                null
                            }
                        }
                    }
                    Tasks.whenAllSuccess<Artist?>(tasks).addOnSuccessListener { documents ->
                        val tracksList = documents.filterNotNull()
                        // Xử lý danh sách trackList ở đây
                        _likedArtist.postValue(tracksList)
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
            }
    }
    fun addArtistToUserFollowArtist ( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.addArtistToUserFollowArtist(userId, trackId)
        }
    }

    fun  removeArtistToUserFollowArtist( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.removeArtistToUserFollowArtist(userId, trackId)
        }
    }
    fun SearchArtistResults(searchQuery: String?) {
        // Gọi phương thức trong Repository để tải dữ liệu từ Firebase dựa trên searchQuery
        val searchResultsLiveData = artistRepository.loadArtistSearchResults(searchQuery)
        // Cập nhật LiveData _tracks với dữ liệu mới
        searchResultsLiveData.observeForever { artists ->
            _artists.postValue(artists) }
    }
    }