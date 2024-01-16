package com.example.weatherconditionapplication.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class WeatherUiState(
    val weatherInfoList: MutableList<WeatherDataInfo> = mutableListOf<WeatherDataInfo>(),
    val locationRequired: MutableState<Boolean> = mutableStateOf(false)

)
