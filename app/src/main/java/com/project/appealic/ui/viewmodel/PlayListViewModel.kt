package com.project.appealic.ui.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.repository.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PlayListViewModel(private val playListRepository: PlayListRepository) :ViewModel() {

    fun createNewPlayList(playList: PlayListEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            playListRepository.createNewPlayList(playList)
        }
    }
    fun getUserPlaylist(uid: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            playListRepository.getAllUserPlayList(uid)
        }
    }

}