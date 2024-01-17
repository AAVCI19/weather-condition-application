package com.example.weatherconditionapplication.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherconditionapplication.data.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherconditionapplication.R
import com.example.weatherconditionapplication.api.WeatherData
import com.example.weatherconditionapplication.data.WeatherDataInfo
import com.example.weatherconditionapplication.data.WeatherUiState
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.fillMaxHeight()

    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${weatherUiState.currentLocation}",
                //text = "${weatherUiState.weatherInfoList[0].temperatureDegree}",
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
            Spacer(modifier = Modifier.height(10.dp))
            WeeklySummaryRow(weatherUiState, modifier)

        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(10.dp)
        ){
            DayDetailsRow(viewModel, weatherUiState.weatherInfoList, modifier)
        }
    }

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
        Spacer(modifier = Modifier.height(5.dp))
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun WeeklySummaryRow(
    weatherUiState: WeatherUiState,
    modifier: Modifier
) {
    LazyRow(
        //columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 10.dp),
    ){
        val daysOfWeek = listOf("TUE", "WED", "THU", "FRI", "SAT", "SUN")
        items(6){
            WeekDayItem(
                weatherUiState = weatherUiState,
                modifier = modifier,
                day = daysOfWeek[it]
            )
        }
    }
}

@Composable
fun WeekDayItem(
    weatherUiState: WeatherUiState,
    modifier: Modifier,
    day: String
) {
    Column(
        modifier = Modifier
            //.fillMaxHeight()
            .fillMaxWidth()
    ){
        Text (
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text (
            text = "Degrees",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayDetailsRow(viewModel: WeatherViewModel, items: List<WeatherDataInfo>, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ){
        Text(
            text = "MONDAY, JANUARY 15",
            //text = "Weather code: ${weatherUiState.weatherInfoList[0].temperatureDegree}",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 10.dp),
        ){
            items(items.take(24)) {
                HourlyRow(viewModel, it, modifier)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyRow(viewModel: WeatherViewModel, weatherDataInfo: WeatherDataInfo, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ){

        var imageRes = R.drawable.sunny
        when (weatherDataInfo.weatherType) {
            in listOf(3,45,48) -> {
                imageRes = R.drawable.foggy
            }
            in listOf(51,53,55,56,57,61,63,65,66,67,80,81,82) -> {
                imageRes = R.drawable.drizzle
            }
            in listOf(71,73,75,77,85,86) -> {
                imageRes = R.drawable.snowy
            }
            in listOf(95,96,99) -> {
                imageRes = R.drawable.thunderstorm
            }
        }

        Text(
            text = viewModel.getHourlySummary(weatherDataInfo).hour,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "${weatherDataInfo.temperatureDegree}°C",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = viewModel.getHourlySummary(weatherDataInfo).desc,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(10.dp))
        Image(
            painter = painterResource(id = viewModel.getHourlySummary(weatherDataInfo).imageResource),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyWeather(
    weatherUiState: WeatherUiState,
    modifier: Modifier,
){
    for (index in weatherUiState.weatherInfoList.indices) {
        if (index < 24){
            Row(
                modifier = modifier.fillMaxWidth()
            ){
                Text( text = weatherUiState.weatherInfoList[index].time.format(
                    DateTimeFormatter.ofPattern("HH")
                ))
                Spacer(modifier = Modifier.width(2.dp))
                Text( text = "${weatherUiState.weatherInfoList[index].temperatureDegree}")
                Spacer(modifier = Modifier.width(2.dp))
            }

        }

    }
}

