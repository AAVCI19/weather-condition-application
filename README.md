# Weather Condition Application
## Overview of Application
#### This android application is developed for Comp319-a project. This is a weather condition application, that fetches live weather data, and access location information while using the app
## Implementation
### API Implementation
#### In this application using Retrofit module, we connected to API endpoint of open-meteo, and send latitude and longitude information of the user to this API. Then we get JSON response
and we parsed it using moshi.json module. Then after parsing data JSON, we mapped temperature values, time values and weathercodes to WeatherResponseData class, for hourly. Then in our viewmodel,
we implement a method that create a list containing WeatherDataInfo class. WeatherDataInfo class consist of time, temperatureDegree and weatherType values for hourly data.
### Location Tracking
#### In this application, for location tracking we used locationCallBack and fusedConnectionClient object from google.gms.location package. Then using manifests, we declared which values we want
to grant to access. Then, in case of no permission, we asked user for accessing. We implemented in the MainActivity. Then after location granted, we continue to access it in background.
### Convert Location to Adress
#### We used Geocoder API from Android.Location.Geocoder to convert latitude and longitude information to address information.
### UI Implementation 
The application contains two main pages: the landing page and the page that includes the weather forecasts. We used the following concepts & classes for better coding principles.
* WeatherViewModel — view model to separate the logic from the UI layer
* WeatherUiState — ui state to store the weather forecasts, user location and current page information
* WeatherDataInfo — data class to store the time, temperature and weather condition of hourly data
We also utilized MaterialTheming to create a color palette for our application and used weather icons for more visuality. To map the weather codes to their corresponding descriptions and icons, we used open-meteo's mapping of WMO Weather interpretation codes (WW).
## Resources
Links to Resources:
* *Open-meteo* : https://open-meteo.com/en/docs#daily=weather_code
## Group Members
#### Ali AVCI 0072779 
#### Ece Pınar ÖZER 0072047
#### Alp Eren Başaran 0076459




