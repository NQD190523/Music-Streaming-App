package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.UserEntity
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel(private val songRepository: SongRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

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



}