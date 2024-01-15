package com.example.weatherconditionapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.weatherconditionapplication.data.FetchData
import com.example.weatherconditionapplication.data.createWeatherInfoList
import com.example.weatherconditionapplication.data.currentLocation
import com.example.weatherconditionapplication.data.locationRequired
import com.example.weatherconditionapplication.data.setCurrentLocation
import com.example.weatherconditionapplication.data.setLocationRequired
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val latitude = 52.52
        val longitude = 13.41
        GlobalScope.launch {
            val response = FetchData().getWeatherData(latitude, longitude)
            val weatherInfo = response?.let { createWeatherInfoList(it) }
            if (weatherInfo != null) {
                Log.d("response", "hour: ${weatherInfo.get(0).time} and temperature: ${weatherInfo.get(0).temperature} and weatherCode:  ")
            }
        }
        fusedConnectionClient = LocationServices.getFusedLocationProviderClient(this)


        setContent{
            var currentLocation by remember {
                mutableStateOf(LatLng(0.0.toDouble(), 0.0.toDouble()))
            }
            locationCallBack = object:LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    for (location in p0.locations){
                        currentLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                locationScreen(this@MainActivity, currentLocation)
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

    @Composable
    private fun locationScreen(context: Context, currentLocation: LatLng){
        val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permission ->
            val granted = permission.values.all { it }
            if(granted){
                setLocationRequired(true)
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
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Your location ${currentLocation.latitude} and ${currentLocation.longitude}")
                Button(onClick = {
                    if(permissions.all {
                            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                        })
                    {
                        startLocationUpdates(fusedConnectionClient)
                    }
                    else {
                        requestPermissionLauncher.launch(permissions)

                    }
                }) {

                    
                }

            }
        }

    }



}

