package com.project.appealic.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "playlists")
@TypeConverters(Converters::class)
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true) var playlistId: Int? = null ,
    val userId: String,
    val playListName: String,
    val playlistThumb: Int,
    val trackIds: List<String>,
    val createdAt: Date = Date()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createStringArrayList()!!,
        TODO("createdAt")
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(playlistId)
        parcel.writeString(userId)
        parcel.writeString(playListName)
        parcel.writeInt(playlistThumb)
        parcel.writeStringList(trackIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayListEntity> {
        override fun createFromParcel(parcel: Parcel): PlayListEntity {
            return PlayListEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlayListEntity?> {
            return arrayOfNulls(size)
        }
    }
}
