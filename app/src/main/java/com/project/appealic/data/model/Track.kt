package com.project.appealic.data.model

import androidx.room.PrimaryKey

data class Track(
    val albumId: String? = null,
    val artistId: String? =null,
    val artist : String? = null,
    val duration: Int? =null,
    val releaseDate : String? =null,
    val trackId : String? = null,
    val trackTitle : String? =null,
    val trackUrl: String? =null,
    val trackImage: String? =null,
    val genre : String? = null
)