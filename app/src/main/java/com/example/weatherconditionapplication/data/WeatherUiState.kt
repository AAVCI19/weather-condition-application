package com.example.weatherconditionapplication.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng

data class WeatherUiState(
    val weatherInfoList: List<WeatherDataInfo> = listOf(),
    val locationRequired: MutableState<Boolean> = mutableStateOf(false),
    //val currentLocation: MutableState<LatLng> = mutableStateOf(LatLng(0.0.toDouble(), 0.0.toDouble())),
    val currentLocation: LatLng = LatLng(0.0.toDouble(), 0.0.toDouble()),
    val isShowingWeatherPage: Boolean = true
)
