package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @SerializedName("address")
    val address: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("error")
    val error: String

)
