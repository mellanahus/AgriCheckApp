package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String? =null,
    @SerializedName("password")
    val pass: String
): Parcelable
