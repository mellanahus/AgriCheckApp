package com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking

import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("signup")
    fun signup(
        @Body user: User
    ): Call<RegisterResponse>

    @POST("signin")
    fun signin(
        @Body user: User
    ):Call<LoginResponse>

    @GET("user")
    fun getUser():Call<ProfileResponse>

    @GET("weather-forecast")
    fun getSearchLocation(
        @Query("location") query: String
    ): Call<WeatherResponse>

    @POST("fert")
    fun getPredictedFertilizer(
        @Body features: Features
    ): Call<FertilizerResponse>

    @Multipart
    @POST("cotton")
    fun getPredictCutton(
        @Part file: MultipartBody.Part,
    ): Call<CuttonResponse>

    @POST("crop")
    fun getPredictCrop(
        @Body features: Features
    ): Call<CropResponse>

    @Multipart
    @POST("soil")
    fun getPredictSoil(
        @Part file: MultipartBody.Part,
    ): Call<SoilResponse>

}