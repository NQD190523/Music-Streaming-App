package com.project.appealic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.ui.viewmodel.ArtistViewModel

class ArtistViewModelFactory(private val artistRepository: ArtistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtistViewModel(artistRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}