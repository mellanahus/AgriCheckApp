package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

import com.google.gson.annotations.SerializedName

data class CropResponse(
    @SerializedName("predicted_crop")
    val predicted_crop:String
)
