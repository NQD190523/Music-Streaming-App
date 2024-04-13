package com.project.appealic.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SongEntity(
    @PrimaryKey val songId : String,
    val thumbUrl: String?,
    val songName: String?,
    val singer: String?,
    val userId : String?,
    val liked : Boolean?,
    val listenedAt : Long?,
    val audioPosition: Long?
)

