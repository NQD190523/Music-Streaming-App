package com.project.appealic.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.ExternalIds
import com.project.appealic.data.model.ExternalUrlsXXX
@Entity
data class Track(
    @PrimaryKey val albumId: String? = null,
    val artistId: String? =null,
    val artist : String? = null,
    val duration: Int? =null,
    val releaseDate : String? =null,
    val trackTitle : String? =null,
    val trackUrl: String? =null,
    val trackImage: String? =null,
    val genre : String? = null
)