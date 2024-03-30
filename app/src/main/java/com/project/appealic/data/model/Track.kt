package com.project.appealic.data.model

import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.ExternalIds
import com.project.appealic.data.model.ExternalUrlsXXX

data class Track(
    val albumId: String? =null,
    val artistId: String? =null,
    val duration: Int? =null,
    val releaseDate : String? =null,
    val title : String? =null,
    val trackUrl: String? =null,
)