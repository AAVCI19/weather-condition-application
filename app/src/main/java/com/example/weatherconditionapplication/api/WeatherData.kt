package com.example.weatherconditionapplication.api

import com.squareup.moshi.Json

data class WeatherData(
    @field:Json(name = "hourly")
    val weatherData: WeatherResponseData
)
