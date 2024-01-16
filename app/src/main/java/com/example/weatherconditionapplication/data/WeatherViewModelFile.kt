package com.example.weatherconditionapplication.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherconditionapplication.api.WeatherData
import com.example.weatherconditionapplication.api.WeatherResponseData
import com.google.android.gms.maps.model.LatLng

fun createWeatherInfoList(WeatherResponseData: WeatherResponseData): List<WeatherDataInfo>{
    val weatherInfoList = mutableListOf<WeatherDataInfo>()
    for (i in 0 until WeatherResponseData.time!!.size) {
        val weatherDataInfo = WeatherDataInfo(
            WeatherResponseData.time!!.get(i),
            WeatherResponseData.temperatures!!.get(i),
            WeatherResponseData.weatherCodes!!.get(i)
        )
        weatherInfoList.add(weatherDataInfo)
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
