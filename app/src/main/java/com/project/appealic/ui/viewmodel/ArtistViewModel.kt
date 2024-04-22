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
            .addOnSuccessListener { tracks ->
                if (tracks != null)
                    _tracks.postValue(tracks.toObjects(Track::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }

    fun getFollowArtistFromUser(userId: String){
        artistRepository.getFollowArtistFromUser(userId)
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val favoriteSongIds = userDoc["followArtist"] as? List<String> ?: emptyList()

                    val tasks = favoriteSongIds.map { artistId ->
                        val trackDocRef = firebaseDB.collection("artists").document(artistId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result?.toObject(Artist::class.java)
                            } else {
                                null
                            }
                        }
                    }
                    Tasks.whenAllSuccess<Artist?>(tasks).addOnSuccessListener { documents ->
                        val artistList = documents.filterNotNull()
                        // Xử lý danh sách artistList ở đây
                        _likedArtist.postValue(artistList)
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
            }
    }

    fun addArtistToUserFollowArtist ( userId: String, artistId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.addArtistToUserFollowArtist(userId, artistId)
        }
    }

    fun removeArtistToUserFollowArtist( userId: String, artistId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.removeArtistToUserFollowArtist(userId, artistId)
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
