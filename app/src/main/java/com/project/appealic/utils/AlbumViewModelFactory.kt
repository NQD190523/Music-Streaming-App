package com.project.appealic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.AlbumRepository
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.ui.viewmodel.AlbumViewModel
import com.project.appealic.ui.viewmodel.ArtistViewModel

class AlbumViewModelFactory(private val albumRepository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumViewModel(albumRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}