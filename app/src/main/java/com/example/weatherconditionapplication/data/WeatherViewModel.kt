package com.example.weatherconditionapplication.data

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherconditionapplication.R
import com.example.weatherconditionapplication.api.WeatherResponseData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class WeatherViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState


    /**
     * createWeatherInfoList() function takes as input a WeatherResponseData and returns the list of WeatherDataInfo
     * This function serves the purpose of initializing the WeatherDataInfo obtained from API in WeatherDataInfo format
     */
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

    data class DailyStats (val minTemp: Double, val maxTemp: Double, val desc: String, val imageResource: Int)
    fun getDailyStats(weatherDataInfoList: List<WeatherDataInfo>) : DailyStats {
        var minTemp: Double = 100.0
        var maxTemp: Double = -100.0

        var weatherTypeMap: MutableMap<Int, Int> = mutableMapOf()

        for (weatherData in weatherDataInfoList) {
            val temp = weatherData.temperatureDegree
            if (temp < minTemp){
                minTemp = temp
            }
            if (temp > maxTemp){
                maxTemp = temp
            }
            if (weatherData.weatherType in weatherTypeMap.keys ){
                val currentCount = weatherTypeMap[weatherData.weatherType]
                if (currentCount != null) {
                    weatherTypeMap[weatherData.weatherType] = currentCount + 1
                }
            } else {
                weatherTypeMap[weatherData.weatherType] = 1
            }
        }

        val maxEntry = weatherTypeMap.maxByOrNull { it.value }

        var keyWithMaxValue = 0

        if (maxEntry != null) {
            keyWithMaxValue = maxEntry.key
        }
        var desc = ""
        var imageRes = R.drawable.sunny

        when (keyWithMaxValue) {
            0 -> { desc = "Clear Sky"; imageRes = R.drawable.sunny }
            1 -> { desc = "Mainly Clear"; imageRes = R.drawable.sunny }
            2 -> { desc = "Partly Cloudy"; imageRes = R.drawable.partly_cloudy }
            3 -> { desc = "Overcast"; imageRes = R.drawable.foggy }
            45 -> { desc = "Foggy"; imageRes = R.drawable.foggy }
            48 -> { desc = "Depositing Rime Fog"; imageRes = R.drawable.foggy }
            51 -> { desc = "Light Drizzle"; imageRes = R.drawable.drizzle }
            53 -> { desc = "Moderate Drizzle"; imageRes = R.drawable.drizzle }
            55 -> { desc = "Dense Drizzle"; imageRes = R.drawable.drizzle }
            56 -> { desc = "Light Freezing Drizzle"; imageRes = R.drawable.drizzle }
            57 -> { desc = "Dense Freezing Drizzle"; imageRes = R.drawable.drizzle }
            61 -> { desc = "Slight Rain"; imageRes = R.drawable.drizzle }
            63 -> { desc = "Moderate Rain"; imageRes = R.drawable.drizzle }
            65 -> { desc = "Heavy Rain"; imageRes = R.drawable.drizzle }
            66 -> { desc = "Light Freezing Drizzle"; imageRes = R.drawable.drizzle }
            67 -> { desc = "Heavy Freezing Rain"; imageRes = R.drawable.drizzle }
            71 -> { desc = "Slight Snow Fall"; imageRes = R.drawable.snowy }
            73 -> { desc = "Moderate Snow Fall"; imageRes = R.drawable.snowy }
            75 -> { desc = "Heavy Snow Fall"; imageRes = R.drawable.snowy }
            77 -> { desc = "Snow Grains"; imageRes = R.drawable.snowy }
            80 -> { desc = "Slight Rain Showers"; imageRes = R.drawable.drizzle }
            81 -> { desc = "Moderate Rain Showers"; imageRes = R.drawable.drizzle }
            82 -> { desc = "Violent Rain Showers"; imageRes = R.drawable.drizzle }
            85 -> { desc = "Slight Snow Showers"; imageRes = R.drawable.snowy }
            86 -> { desc = "Heavy Snow Showers"; imageRes = R.drawable.snowy }
            95 -> { desc = "Moderate Thunderstorm"; imageRes = R.drawable.thunderstorm }
            96 -> { desc = "Slight Hail Thunderstorm"; imageRes = R.drawable.thunderstorm }
            99 -> { desc = "Heavy Hail Thunderstorm"; imageRes = R.drawable.thunderstorm }
            else -> { desc = "Clear Sky"; imageRes = R.drawable.sunny }
        }


        return DailyStats(minTemp, maxTemp, desc, imageRes)

    }
    /**
     * getHourlySummary() function takes as input a WeatherDataInfo instance and returns the following:
     *
     * hour : hour information with AM/PM  (ex: 12 PM)
     * desc : weather forecast description based on the weatherType  (ex: "Mainly Clear)
     * imageResource : corresponding image resource ID for the weatherType
     *
     * Result data class is created to output hour, desc, and imageResource values at the same time
     * Weather codes and the corresponding descriptions are taken from the API documentation
     */
    data class Result (val hour: String, val desc: String, val imageResource: Int)
    fun getHourlySummary(weatherDataInfo: WeatherDataInfo): Result {
        var hour: String = ""
        var desc: String = ""
        var imageRes = R.drawable.sunny

        val hourChars = weatherDataInfo.time[(weatherDataInfo.time.length-5)].toString() + weatherDataInfo.time[(weatherDataInfo.time.length-4)].toString()
        //hour = hourChars

        hour = if (hourChars.toInt() > 11) {
            val mod12 = hourChars.toInt() % 12
            if (mod12 == 0) {
                "12 PM"
            } else {
                "$mod12 PM"
            }
        } else {
            "${hourChars.toInt()} AM"
        }


        when (weatherDataInfo.weatherType) {
            0 -> { desc = "Clear Sky"; imageRes = R.drawable.sunny }
            1 -> { desc = "Mainly Clear"; imageRes = R.drawable.sunny }
            2 -> { desc = "Partly Cloudy"; imageRes = R.drawable.partly_cloudy }
            3 -> { desc = "Overcast"; imageRes = R.drawable.foggy }
            45 -> { desc = "Foggy"; imageRes = R.drawable.foggy }
            48 -> { desc = "Depositing Rime Fog"; imageRes = R.drawable.foggy }
            51 -> { desc = "Light Drizzle"; imageRes = R.drawable.drizzle }
            53 -> { desc = "Moderate Drizzle"; imageRes = R.drawable.drizzle }
            55 -> { desc = "Dense Drizzle"; imageRes = R.drawable.drizzle }
            56 -> { desc = "Light Freezing Drizzle"; imageRes = R.drawable.drizzle }
            57 -> { desc = "Dense Freezing Drizzle"; imageRes = R.drawable.drizzle }
            61 -> { desc = "Slight Rain"; imageRes = R.drawable.drizzle }
            63 -> { desc = "Moderate Rain"; imageRes = R.drawable.drizzle }
            65 -> { desc = "Heavy Rain"; imageRes = R.drawable.drizzle }
            66 -> { desc = "Light Freezing Drizzle"; imageRes = R.drawable.drizzle }
            67 -> { desc = "Heavy Freezing Rain"; imageRes = R.drawable.drizzle }
            71 -> { desc = "Slight Snow Fall"; imageRes = R.drawable.snowy }
            73 -> { desc = "Moderate Snow Fall"; imageRes = R.drawable.snowy }
            75 -> { desc = "Heavy Snow Fall"; imageRes = R.drawable.snowy }
            77 -> { desc = "Snow Grains"; imageRes = R.drawable.snowy }
            80 -> { desc = "Slight Rain Showers"; imageRes = R.drawable.drizzle }
            81 -> { desc = "Moderate Rain Showers"; imageRes = R.drawable.drizzle }
            82 -> { desc = "Violent Rain Showers"; imageRes = R.drawable.drizzle }
            85 -> { desc = "Slight Snow Showers"; imageRes = R.drawable.snowy }
            86 -> { desc = "Heavy Snow Showers"; imageRes = R.drawable.snowy }
            95 -> { desc = "Moderate Thunderstorm"; imageRes = R.drawable.thunderstorm }
            96 -> { desc = "Slight Hail Thunderstorm"; imageRes = R.drawable.thunderstorm }
            99 -> { desc = "Heavy Hail Thunderstorm"; imageRes = R.drawable.thunderstorm }
            else -> { desc = "Clear Sky"; imageRes = R.drawable.sunny }
        }

        return Result(hour, desc, imageRes)
    }

    @Suppress("DEPRECATION")
    fun SetLocationAdress(context: Context, location: LatLng){
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(41.112663, 29.021330, 1)
        if(addresses != null){
            if(addresses.isNotEmpty()){
                val address = addresses[0]
                val input = address.getAddressLine(0)
                if(input.isNotEmpty()){
                    _uiState.update{
                        it.copy(
                            locationAdress = input
                        )
                    }
                }
                else{
                    _uiState.update{
                        it.copy(
                            locationAdress = "Not available adress"
                        )
                    }
                }
            }
            else{
                _uiState.update{
                    it.copy(
                        locationAdress = "empty adress"
                    )
                }
            }
        }
        else{
            _uiState.update{
                it.copy(
                    locationAdress = "null adress"
                )
            }
        }

    }
}