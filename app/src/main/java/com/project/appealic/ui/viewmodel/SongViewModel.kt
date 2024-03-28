package com.project.appealic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers

class SongViewModel(private val repository: SongRepository) : ViewModel() {

    val tracks = liveData(Dispatchers.IO) {
        val retrievedSongs = repository.getAllTrack()
        emit(retrievedSongs)
    }

}