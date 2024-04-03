package com.project.appealic.data.model

import com.google.gson.annotations.SerializedName

data class AccessTokenRespone(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long
)