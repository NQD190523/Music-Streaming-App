package com.project.appealic.data.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "playlists")
@TypeConverters(PlayListEntity.TrackListConverter::class)
data class PlayListEntity(
    @PrimaryKey val playListId: String = SimpleDateFormat(
        "yyyyMMdd_HHmmss", Locale.getDefault()
    ).format(Date()),
    val uid: String,
    val playListName: String,
    val playListThumb: Int,
    val tracks: MutableList<Track> = mutableListOf()
) {
    class TrackListConverter {
        @TypeConverter
        fun fromTracks(tracks: MutableList<Track>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Track>>() {}.type
            return gson.toJson(tracks, type)
        }

        @TypeConverter
        fun toTracks(tracksJson: String): MutableList<Track> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Track>>() {}.type
            return gson.fromJson(tracksJson, type)
        }

    }
}