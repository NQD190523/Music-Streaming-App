package com.project.appealic.data.model

import android.os.Parcel
import android.os.Parcelable

data class Playlist(
    val playlistArtists: String?,
    val playlistId: String,
    val playlistName: String,
    val playlistThumb: String?,
    val trackIds: List<String>?
) : Parcelable {

    // Constructor không đối số
    constructor() : this("", "", "", null, null)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(playlistArtists)
        parcel.writeString(playlistId)
        parcel.writeString(playlistName)
        parcel.writeString(playlistThumb)
        parcel.writeStringList(trackIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}