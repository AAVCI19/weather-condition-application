package com.example.weatherconditionapplication.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherconditionapplication.api.WeatherData
import com.google.android.gms.maps.model.LatLng

fun createWeatherInfoList(weatherData: WeatherData): List<WeatherInfo>{
    val weatherInfoList = mutableListOf<WeatherInfo>()
    for ((index, value) in weatherData.time?.withIndex()!!){
        weatherInfoList.add(WeatherInfo(weatherData.temperature_2m?.get(index), value))
    }
    return weatherInfoList
}

private val _weatherInfo = MutableLiveData<WeatherData>()
val weatherInfo: LiveData<WeatherData> get() = _weatherInfo

private val _locationRequired = mutableStateOf(false)
val locationRequired = _locationRequired

fun setLocationRequired(value: Boolean) {
    _locationRequired.value = value
}

private val _currentLocation = mutableStateOf(LatLng(0.0.toDouble(), 0.0.toDouble()))
val currentLocation = _currentLocation
fun setCurrentLocation(value: LatLng){
    _currentLocation.value = value
}
