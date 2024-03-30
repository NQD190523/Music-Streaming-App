package com.project.appealic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.SongRepository

class SongViewModelFactory(private val songRepository: SongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(songRepository) as T
        }
        throw IllegalAccessException("Unknow ViewModel class")
    }
}