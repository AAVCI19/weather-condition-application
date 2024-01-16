package com.example.weatherconditionapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.weatherconditionapplication.data.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
){
    val viewModel: WeatherViewModel = viewModel()
    val detailUiState = viewModel.uiState.collectAsState().value

}