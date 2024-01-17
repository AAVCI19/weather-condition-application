package com.example.weatherconditionapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherconditionapplication.data.FetchData
import com.example.weatherconditionapplication.data.WeatherUiState
import com.example.weatherconditionapplication.data.WeatherViewModel
import com.example.weatherconditionapplication.ui.WeatherScreen
import com.example.weatherconditionapplication.ui.theme.AppTheme
import com.example.weatherconditionapplication.ui.theme.AppTypography
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var fusedConnectionClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback

    private val _locationRequired = mutableStateOf(false)
    val locationRequired = _locationRequired

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedConnectionClient = LocationServices.getFusedLocationProviderClient(this)


        setContent{
            val viewModel: WeatherViewModel = viewModel()
            val weatherUiState = viewModel.uiState.collectAsState().value

            var currentLocation by remember {
                mutableStateOf(LatLng(0.0.toDouble(), 0.0.toDouble()))
            }
            locationCallBack = object:LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    for (location in p0.locations){
                        currentLocation = LatLng(location.latitude, location.longitude)
                        viewModel.setCurrentLocation(currentLocation )
                    }
                    GlobalScope.launch {
                        val response = FetchData().getWeatherData(41.112663, 29.021330)

                        val weatherInfo = response?.let {
                            viewModel.createWeatherInfoList(it)
                        }
                        if (weatherInfo != null) {
                            Log.d("response", "Time: ${weatherInfo.get(0).time}, Temperature: ${weatherInfo.get(0).temperatureDegree} WeatherCode: ${weatherInfo.get(0).weatherType}")
                        }
                    }
                }
            }
            AppTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    locationScreen(this@MainActivity, viewModel, weatherUiState)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired.value){
            startLocationUpdates(fusedConnectionClient)
        }
    }
    override fun onPause(){
        super.onPause()
        locationCallBack?.let {
            fusedConnectionClient?.removeLocationUpdates(it)
        }
    }
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedConnectionClient: FusedLocationProviderClient){
        locationCallBack?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()
            fusedConnectionClient?.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun locationScreen(context: Context, viewModel: WeatherViewModel, weatherUiState: WeatherUiState){

        if (weatherUiState.currentLocation != LatLng(0.0,0.0)){
            WeatherScreen(viewModel = viewModel, weatherUiState = weatherUiState)
        } else {
            val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                    permission ->
                val granted = permission.values.all { it }
                if(granted){
                    viewModel.setLocationRequired(true)
                    startLocationUpdates(fusedConnectionClient)
                    Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            val permissions = arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )


                Box(modifier = Modifier.fillMaxSize()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        //verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.partly_cloudy),
                            contentDescription = null,
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        MaterialTheme(
                            typography = AppTypography
                        ) {
                            Text(
                                text = "Welcome to..",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Weather App",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = "With this App, you can see the daily and weekly weather forecast for your current location",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                            if (permissions.all {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        it
                                    ) == PackageManager.PERMISSION_GRANTED
                                }) {
                                startLocationUpdates(fusedConnectionClient)
                                viewModel.setIsShowingWeatherPage(true)
                                viewModel.SetLocationAdress(context, LatLng(weatherUiState.currentLocation.latitude, weatherUiState.currentLocation.longitude))

                            } else {
                                requestPermissionLauncher.launch(permissions)
                            }
                        }) {
                            Text(
                                text = "Show Weather Forecast",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                        }

                    }
                }
            }
    }

}

