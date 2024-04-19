package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.UserEntity
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

class SongViewModel(private val songRepository: SongRepository, private val userRepository: UserRepository) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _tracks = MutableLiveData<List<Track>>()
    private val _likedSongs = MutableLiveData<List<Track>>()
    val likedSongs: LiveData<List<Track>> get() = _likedSongs

    val tracks: LiveData<List<Track>> get() = _tracks

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    fun getAllTracks(){
        songRepository.getAllTrack()
            .addOnSuccessListener { tracks ->
                    if(tracks != null) _tracks.postValue(tracks.toObjects(Track::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error",exception.toString())
            }
    }

    fun getAllArtists(){
        songRepository.getAllArtist()
            .addOnSuccessListener { artists ->
                if(artists!= null)
                    _artists.postValue(artists.toObjects(Artist::class.java))
            }
            .addOnFailureListener { exception->
                Log.e("error" , exception.toString())
            }
    }

    fun getAllPlaylists() {
        songRepository.getAllPlaylists()
            .addOnSuccessListener { playlists ->
                if (playlists != null)
                    _playlists.postValue(playlists.toObjects(Playlist::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }


    fun getTrackFromGenres(genre :String){
        songRepository.getAllTrack()
            .addOnSuccessListener { tracks ->
                val genreTrack = tracks.documents.filter { it.toObject(Track::class.java)?.genre == genre }
                _tracks.postValue(genreTrack.map { it.toObject(Track::class.java)!! })
            }
    }

    fun getUserData() : LiveData<UserEntity>? {
        return userRepository.getUserData()
    }

    fun getRecentSongs(userId: String): LiveData<List<SongEntity>> {
        return songRepository.getRecentSongs(userId)
    }

    fun insertSong(song: SongEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.insertSong(song)
        }
    }


    fun getLikedSongs(userId: String) : CompletableFuture<List<Track>>{
       return songRepository.getLikedSongFromUser(userId)
    }

    fun addTrackToUserLikedSongs ( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.addTrackToUserLikedSongs(userId, trackId)
        }
    }

    fun removeTrackFromUserLikedSongs ( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.removeTrackFromUserLikedSongs(userId, trackId)
        }
    }

    fun getAllAlbums() {
        songRepository.getAllAlbums()
            .addOnSuccessListener { albums ->
                if (albums != null)
                    _albums.postValue(albums.toObjects(Album::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }

    fun loadSearchResults(searchQuery: String?) {
        // Gọi phương thức trong Repository để tải dữ liệu từ Firebase dựa trên searchQuery
        val searchResultsLiveData = songRepository.loadSearchResults(searchQuery)
        // Cập nhật LiveData _tracks với dữ liệu mới
        searchResultsLiveData.observeForever { searchResultsLiveData ->
            _tracks.postValue(searchResultsLiveData.tracks)
            _artists.postValue(searchResultsLiveData.artist)
            _playlists.postValue(searchResultsLiveData.playlists)
            _albums.postValue(searchResultsLiveData.albums)
        }
}
}