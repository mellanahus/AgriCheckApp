package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

import com.google.gson.annotations.SerializedName

data class FertilizerResponse(
    @SerializedName("predicted_fert")
    val predicted_fert:String
)
