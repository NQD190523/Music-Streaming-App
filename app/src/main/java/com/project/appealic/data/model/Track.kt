package com.project.appealic.data.model

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {

    // Implement các phương thức của Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(albumId)
        parcel.writeString(artistId)
        parcel.writeString(artist)
        parcel.writeValue(duration)
        parcel.writeString(releaseDate)
        parcel.writeString(trackId)
        parcel.writeString(trackTitle)
        parcel.writeString(trackUrl)
        parcel.writeString(trackImage)
        parcel.writeString(genre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(
                albumId = parcel.readString(),
                artistId = parcel.readString(),
                artist = parcel.readString(),
                duration = parcel.readValue(Int::class.java.classLoader) as? Int,
                releaseDate = parcel.readString(),
                trackId = parcel.readString(),
                trackTitle = parcel.readString(),
                trackUrl = parcel.readString(),
                trackImage = parcel.readString(),
                genre = parcel.readString()
            )
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}