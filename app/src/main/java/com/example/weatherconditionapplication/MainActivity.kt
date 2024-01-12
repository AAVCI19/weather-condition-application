package com.example.weatherconditionapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherconditionapplication.api.RetrofitHelper
import com.example.weatherconditionapplication.api.WeatherApi
import com.example.weatherconditionapplication.ui.theme.WeatherConditionApplicationTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val latitude = 52.52
        val longitude = 13.41
        GlobalScope.launch {
            val response = RetrofitHelper.weatherApiService.getWeatherResponseData(latitude, longitude)
            if (response.isSuccessful){
                Log.d("deneme:", response.body().toString())
            }
        }


    }
}

