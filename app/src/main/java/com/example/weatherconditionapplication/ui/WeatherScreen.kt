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
import androidx.compose.foundation.layout.size
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.util.Calendar

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
                .padding(10.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.size(10.dp))
            Row(modifier = modifier){
                Image(
                    painter = painterResource(id = R.drawable.pin),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                )
                Column(
                    modifier = modifier
                        .padding(10.dp)
                ){
                    Text(
                        text = "${weatherUiState.locationAdress}",
                        //text = "${weatherUiState.weatherInfoList[0].temperatureDegree}",
                        //text = "Uskumruköy",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "(${weatherUiState.currentLocation.latitude} ${weatherUiState.currentLocation.longitude})",
                        //text = "${weatherUiState.weatherInfoList[0].temperatureDegree}",
                        //text = "Uskumruköy",
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

            }

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
            DailySummaryRow(viewModel,weatherUiState, modifier)
            Spacer(modifier = Modifier.height(10.dp))
            WeeklySummaryRow(viewModel,weatherUiState, modifier)

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
    viewModel: WeatherViewModel,
    weatherUiState: WeatherUiState,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ){

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            var dailyWeather = weatherUiState.weatherInfoList.take(24)
            val avgTemp = (viewModel.getDailyStats(dailyWeather).maxTemp + viewModel.getDailyStats(dailyWeather).minTemp) / 2

            Column(modifier = modifier ){
                Text(
                    text = "${avgTemp}°C",
                    //text = "${weatherUiState.weatherInfoList[0].temperatureDegree}°C",
                    fontSize = 50.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = viewModel.getDailyStats(dailyWeather).desc,
                    //text = "Weather code: ${weatherUiState.weatherInfoList[0].temperatureDegree}",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Image(
                painter = painterResource(id = R.drawable.sunny),
                contentDescription = null,
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklySummaryRow(
    viewModel: WeatherViewModel,
    weatherUiState: WeatherUiState,
    modifier: Modifier
) {
    LazyRow(
        //columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        val currentDate = LocalDate.now().dayOfWeek
        val longOne: Long = 1L

        items(6){

            var dailyWeather = weatherUiState.weatherInfoList.take(24 * (it + 1)).takeLast(24)

            WeekDayItem(
                weatherUiState = weatherUiState,
                modifier = modifier,
                day = currentDate + (longOne * it + 1),
                maxTemp = viewModel.getDailyStats(dailyWeather).maxTemp,
                minTemp = viewModel.getDailyStats(dailyWeather).minTemp,
                imageRes = viewModel.getDailyStats(dailyWeather).imageResource
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
fun WeekDayItem(
    weatherUiState: WeatherUiState,
    modifier: Modifier,
    day: DayOfWeek,
    maxTemp: Double,
    minTemp: Double,
    imageRes: Int
) {
    Column(
        modifier = Modifier
            //.fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text (
            text = day.toString().take(3),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text (
            text = "$maxTemp°C",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text (
            text = "$minTemp°C",
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

        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)).toString(),
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer

        )
        Spacer(modifier = Modifier.height(5.dp))
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


