package com.project.appealic.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Artist(
    val Bio : String? = null,
    val Name: String? = null,
    val Id : String? = null,
    val ImageResource: String? = null
): Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Bio)
        parcel.writeString(Name)
        parcel.writeString(Id)
        parcel.writeString(ImageResource)
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(
                Bio = parcel.readString(),
                Name = parcel.readString(),
                Id = parcel.readString(),
                ImageResource = parcel.readString()
            )

        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }

}