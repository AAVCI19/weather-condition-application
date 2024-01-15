package com.example.weatherconditionapplication.api

data class WeatherData(
    val temperature_2m: List<Double>?,
    val time: List<String>?,
    val weatherCode: List<Int>?
)
