package com.example.weatherconditionapplication.api

import android.health.connect.datatypes.units.Temperature
import com.example.weatherconditionapplication.api.WeatherData
import com.squareup.moshi.Json

data class WeatherResponseData(
    val time: List<String>?,
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>?,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>?

)
