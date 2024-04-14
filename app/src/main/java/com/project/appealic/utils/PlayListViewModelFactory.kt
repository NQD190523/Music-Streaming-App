package com.project.appealic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.viewmodel.PlayListViewModel

class PlayListViewModelFactory(private val playListRepository: PlayListRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PlayListViewModel(playListRepository) as T
        }
        throw IllegalAccessException("Unknow ViewModel class")
    }
}