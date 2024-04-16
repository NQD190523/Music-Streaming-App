package com.project.appealic.ui.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.UserPlaylist
import com.project.appealic.data.model.UserWithPlayLists
import com.project.appealic.data.repository.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PlayListViewModel(private val playListRepository: PlayListRepository) :ViewModel() {
    private val _userPlayLists = MutableLiveData<List<PlayListEntity>>()
    val userPlayLists: LiveData<List<PlayListEntity>> get() = _userPlayLists

    private val _playLists = MutableLiveData<List<Playlist>>()
    val playLists: LiveData<List<Playlist>> get() = _playLists
    fun createNewPlayList(playList: PlayListEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            playListRepository.createNewPlayList(playList)
        }
    }
    fun getUserPlaylist(uid: String) = viewModelScope.launch {
        val playLists = withContext(Dispatchers.IO) {
            playListRepository.getAllUserPlayList(uid)
        }
        _userPlayLists.postValue(playLists)
    }
    fun getAllPlayList(){
        playListRepository.getAllTrack()
            .addOnCompleteListener { playlist ->
        }
    }
}