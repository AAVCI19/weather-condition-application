package com.example.weatherconditionapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {
    val baseUrl = "https://api.open-meteo.com/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val weatherApiService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}