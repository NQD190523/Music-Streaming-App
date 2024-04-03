package com.project.appealic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository

class SongViewModelFactory(private val songRepository: SongRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return  SongViewModel(songRepository, userRepository) as T
        }
        throw IllegalAccessException("Unknow ViewModel class")
    }
}