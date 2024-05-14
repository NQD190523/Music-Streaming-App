package com.project.appealic.data.model

import android.os.Parcel
import android.os.Parcelable
data class Album(
    val albumId: String?,
    val artistId: String?,
    val artistName: String?,
    val releaseDate: String?,
    val thumbUrl: String?,
    val title: String?,
    val trackIds: List<String>?
) : Parcelable {

    // Default constructor
    constructor() : this(null, null, null, null, null, null, null)

    // Constructor that takes a Parcel and gives you an object populated with its values
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(albumId)
        parcel.writeString(artistId)
        parcel.writeString(artistName)
        parcel.writeString(releaseDate)
        parcel.writeString(thumbUrl)
        parcel.writeList(trackIds)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}