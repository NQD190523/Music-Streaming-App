package com.project.appealic.data.model

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Album(
    val albumId: String,
    val artistId: String,
    val artistName: String,
    val releaseDate: String,
    val thumbUrl: String?,
    val title: String,
    val trackIds : List<String>?
) : Parcelable {
    constructor() : this("", "", "", "", null,"", null)
}