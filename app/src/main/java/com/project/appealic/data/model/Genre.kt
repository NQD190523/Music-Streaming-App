package com.project.appealic.data.model

import android.os.Parcel
import android.os.Parcelable

data class Genre(
    val id: Int,
    val name: String?
) : Parcelable {

    constructor() : this(0, "")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }
    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Genre> {
        override fun createFromParcel(parcel: Parcel): Genre {
            return Genre(parcel)
        }

        override fun newArray(size: Int): Array<Genre?> {
            return arrayOfNulls(size)
        }
    }
}