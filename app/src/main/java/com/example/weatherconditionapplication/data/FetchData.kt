package com.example.weatherconditionapplication.data

import com.example.weatherconditionapplication.api.RetrofitHelper
import com.example.weatherconditionapplication.api.WeatherData

class FetchData {
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherData? {
        val response = RetrofitHelper.weatherApiService.getWeatherResponseData(latitude, longitude)
        if(response.isSuccessful){
            val temperature_2m = response.body()?.hourly?.temperature_2m
            val time = response.body()?.hourly?.time
            return WeatherData(temperature_2m, time)
        }
        else{
            return null
        }
    }
}