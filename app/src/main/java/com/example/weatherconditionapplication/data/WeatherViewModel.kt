package com.example.weatherconditionapplication.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherconditionapplication.api.WeatherResponseData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WeatherViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    //fun createWeatherInfoList(weatherResponseData: WeatherResponseData) {
    fun createWeatherInfoList(weatherResponseData: WeatherResponseData) : List<WeatherDataInfo>{
        val weatherInfoList = mutableListOf<WeatherDataInfo>()
        for (i in 0 until weatherResponseData.time!!.size) {
            val weatherDataInfo = WeatherDataInfo(
                weatherResponseData.time!!.get(i),
                weatherResponseData.temperatures!!.get(i),
                weatherResponseData.weatherCodes!!.get(i)
            )
            weatherInfoList.add(weatherDataInfo)
        }
        _uiState.update{
            it.copy(
                weatherInfoList = weatherInfoList
            )
        }
        return weatherInfoList
    }


    fun setLocationRequired(value: Boolean) {
        _uiState.update{

            it.copy(
                locationRequired = mutableStateOf(value)
            )
        }

    }
    fun setCurrentLocation(location: LatLng) {
        _uiState.update{

            it.copy(
                currentLocation = location
            )
        }
    }

    fun setIsShowingWeatherPage(boolean: Boolean){
        _uiState.update{

            it.copy(
                isShowingWeatherPage = boolean
            )
        }
    }
    /*

    private val _weatherInfo = MutableLiveData<WeatherData>()
    val weatherInfo: LiveData<WeatherData> get() = _weatherInfo



    private val _currentLocation = mutableStateOf(LatLng(0.0.toDouble(), 0.0.toDouble()))
    val currentLocation = _currentLocation
    fun setCurrentLocation(value: LatLng){
        _currentLocation.value = value
    }

     */

}