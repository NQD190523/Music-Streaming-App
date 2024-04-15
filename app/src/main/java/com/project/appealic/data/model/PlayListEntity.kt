package com.project.appealic.data.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class PlayListEntity(
    @PrimaryKey  val playListId : String = SimpleDateFormat(
        "yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()
    ),
    val uid : String,
    val playListName : String,
    val playListThumb : ByteArray?,
    val songId : String?
)
