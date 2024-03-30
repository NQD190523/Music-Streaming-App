package com.project.appealic.data.model

import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.ExternalIds
import com.project.appealic.data.model.ExternalUrlsXXX

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val disc_number: Int,
    val duration_ms: Int,
    val external_ids: ExternalIds,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: Any,
    val track_number: Int,
    val uri: String
)