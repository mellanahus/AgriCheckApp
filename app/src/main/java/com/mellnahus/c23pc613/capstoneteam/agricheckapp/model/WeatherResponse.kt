package com.mellnahus.c23pc613.capstoneteam.agricheckapp.model

data class WeatherResponse(
    val location: String,
    val forecast: List<Forecast>
)

data class Forecast(
    val date: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val text: String
)

