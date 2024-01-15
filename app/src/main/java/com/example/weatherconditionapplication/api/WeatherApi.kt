package com.example.weatherconditionapplication.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast?hourly=temperature_2m")
    suspend fun getWeatherResponseData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<WeatherResponseData>
}