package com.example.weatherconditionapplication.data

import java.time.LocalDateTime

data class WeatherDataInfo(
    val time: String,
    val temperatureDegree: Double,
    val weatherType: Int,
)
