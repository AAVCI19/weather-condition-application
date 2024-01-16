package com.example.weatherconditionapplication.data

import com.example.weatherconditionapplication.api.RetrofitHelper
import com.example.weatherconditionapplication.api.WeatherData
import com.example.weatherconditionapplication.api.WeatherResponseData

class FetchData {
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherResponseData? {
        val response = RetrofitHelper.weatherApiService.getWeatherResponseData(latitude, longitude)
        if(response.isSuccessful){
            val time = response.body()?.weatherData?.time
            val temperatures = response.body()?.weatherData?.temperatures
            val weatherCode = response.body()?.weatherData?.weatherCodes
            return WeatherResponseData(time,temperatures, weatherCode)
        }
        else{
            return null
        }
    }
}