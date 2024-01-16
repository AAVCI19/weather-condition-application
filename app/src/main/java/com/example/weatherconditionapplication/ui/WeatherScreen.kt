package com.example.weatherconditionapplication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherconditionapplication.data.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherconditionapplication.data.WeatherUiState

@Composable
fun WeatherScreen(
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.fillMaxHeight()

    ){
        Row(
            modifier = modifier.fillMaxWidth().padding(0.dp,10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "${weatherUiState.currentLocation}",
                //text = "Uskumruköy",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(10.dp),
        ){
            //Text (text = "Hello")
            DailySummaryRow(weatherUiState, modifier)
            //WeeklySummaryRow(weatherUiState, modifier)
        }
    }

}

@Composable
fun WeeklySummaryRow(weatherUiState: WeatherUiState, modifier: Modifier) {
    TODO("Not yet implemented")
}

@Composable
fun DailySummaryRow(
    weatherUiState: WeatherUiState,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ){

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "9°C",
                //text = "${weatherUiState.weatherInfoList[0].temperatureDegree}°C",
                fontSize = 50.sp,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Mostly sunny",
                //text = "Weather code: ${weatherUiState.weatherInfoList[0].temperatureDegree}",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )


    }

}

