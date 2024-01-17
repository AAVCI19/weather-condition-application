package com.example.weatherconditionapplication.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng

data class WeatherUiState(
    val weatherInfoList: List<WeatherDataInfo> = listOf(),
    val locationRequired: MutableState<Boolean> = mutableStateOf(false),
    val currentLocation: LatLng = LatLng(0.0, 0.0),
    val isShowingWeatherPage: Boolean = true,
    val locationAdress: String = "Istanbul"
)
