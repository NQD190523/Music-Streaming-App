package com.project.appealic.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SongEntity(
    @PrimaryKey val songId: String,
    val songUrl: String?,
    val thumbUrl: String?,
    val songName: String?,
    val singer: String?,
    val userId: String?,
    val liked: Boolean?,
    val listenedAt: Long?,
    val audioPosition: Long?,
    val duration: Long?,
    val artistId: String?,
    val albumId: String?,
    val genre: String?,
    val releaseDate: String?
)

