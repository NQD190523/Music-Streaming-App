package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.toObject
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _artists = MutableLiveData<Artist>()
    val artists: LiveData<Artist> get() = _artists

    init {
        loadSongs()
    }

    private fun loadSongs() {
        songRepository.getAllTrack { tracks ->
            _tracks.postValue(tracks)
        }
    }

    fun getArtists(artistId : String){
        songRepository.getArtist(artistId)
            .addOnSuccessListener { document ->
                if(document != null)
                    _artists.postValue(document.toObject(Artist::class.java))
            }
            .addOnFailureListener { exception->
                Log.e("error" , exception.toString())
            }
    }



}