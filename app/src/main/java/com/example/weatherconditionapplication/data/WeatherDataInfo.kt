package com.example.weatherconditionapplication.data

/**
 * main data class to store the Weather forecast information in a formatted way
 * Its attributes include time, temperature and the weather type (number indicating the weather condition).
 */
data class WeatherDataInfo(
    val time: String,
    val temperatureDegree: Double,
    val weatherType: Int,
)
