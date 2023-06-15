package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("uid")
    val uid: String
)
