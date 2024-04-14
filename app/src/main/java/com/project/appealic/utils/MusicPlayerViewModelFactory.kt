package com.project.appealic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel

class MusicPlayerViewModelFactory(private val musicPlayerService: MusicPlayerService):ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(MusicPlayerViewModel::class.java)){
//            return MusicPlayerViewModel(musicPlayerService) as T
//        }
//        throw IllegalAccessException("unknow viewmodel class")
//    }
}