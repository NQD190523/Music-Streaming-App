package com.project.appealic.data.model

data class Album(
    val artistId: String,
    val artistName: String,
    val releaseDate: String,
    val thumbUrl: String,
    val title: String,
    val trackIds : List<String>?
){
    constructor() : this("","","","","",null)
}