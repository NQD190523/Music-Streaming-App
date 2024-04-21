package com.project.appealic.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "playlists")
@TypeConverters(Converters::class)
data class PlayListEntity(
    @PrimaryKey var playlistId: String,
    val userId: String,
    val playListName: String,
    val playlistThumb: Int,
    val trackIds: List<String>,
    val createdAt: Date = Date()
)