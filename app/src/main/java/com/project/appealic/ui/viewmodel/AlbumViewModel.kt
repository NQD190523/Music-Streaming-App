package com.project.appealic.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.firestore
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.AlbumRepository

class AlbumViewModel( private val albumRepository: AlbumRepository) : ViewModel() {

    private val firebaseDB = Firebase.firestore
    private val _album = MutableLiveData<List<Album>>()
    val album: LiveData<List<Album>> get() = _album

    private val _tracks = MutableLiveData<List<Track>>()
    val track: LiveData<List<Track>> get() = _tracks

    fun getAllAlbum() {
        albumRepository.getAllAlbums()
            .addOnSuccessListener { album ->
                if (album != null) _album.postValue(album.toObjects(Album::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }
    fun getTracksFromAlbum(albumId: String){
        albumRepository.getTracksFromAlbum(albumId)
            .addOnSuccessListener { album ->
                if (album.exists()) {
                    val albumId = album["trackIds"] as? List<String> ?: emptyList()

                    val tasks = albumId.map { trackId ->
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
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
            }
    }
    fun SearchAlbumResults(searchQuery: String?) {
        // Gọi phương thức trong Repository để tải dữ liệu từ Firebase dựa trên searchQuery
        val searchResultsLiveData = albumRepository.loadAlbumSearchResults(searchQuery)
        // Cập nhật LiveData _tracks với dữ liệu mới
        searchResultsLiveData.observeForever { albums ->
            _album.postValue(albums) }
    }
}