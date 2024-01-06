package com.example.jetweatherforcast.ui.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetweatherforcast.data.DataOrException
import com.example.jetweatherforcast.model.Weather
import com.example.jetweatherforcast.model_city.OpenCageResponse
import com.example.jetweatherforcast.navigation.WeatherScreens
import com.example.jetweatherforcast.ui.screens.settings.SettingsViewModel
import com.example.jetweatherforcast.utils.Constants.API_KEY
import com.example.jetweatherforcast.utils.Constants.IMAGE_URL
import com.example.jetweatherforcast.utils.formatDate
import com.example.jetweatherforcast.utils.formatDecimals
import com.example.jetweatherforcast.widgets.HumidityWindPressureRow
import com.example.jetweatherforcast.widgets.SunsetSunRiseRow
import com.example.jetweatherforcast.widgets.WeatherAppBar
import com.example.jetweatherforcast.widgets.WeatherDetailRow
import com.example.jetweatherforcast.widgets.WeatherStateImage

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
) {

    val unitFromDB = settingsViewModel.unitList.collectAsState().value
    var unit by remember { mutableStateOf("imperial") }
    var isImperial by remember { mutableStateOf(false) }

    val cityQuery = city ?: ""
    val latState = remember { mutableDoubleStateOf(41.006381) }
    val lonState = remember { mutableDoubleStateOf(28.9758715) }

    val mapData by produceState<DataOrException<OpenCageResponse, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.getCityCoordinates(
            cityName = cityQuery
        )
    }

    if (mapData.data != null && mapData.data!!.results.isNotEmpty()) {
        latState.doubleValue = mapData.data!!.results[0].geometry.lat
        lonState.doubleValue = mapData.data!!.results[0].geometry.lng
        Log.d("MainScreen", "MainScreen: ${latState.doubleValue}, ${lonState.doubleValue}")
    } else {
        // Handle the case when mapData.data or mapData.data.results is null or empty
        // Show an appropriate error message or handle the error
    }

    if (unitFromDB.isNotEmpty()) {
        unit = unitFromDB[0].unit
        isImperial = unit == "imperial"
        val weatherData by produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.getWeatherData(
                lat = latState.doubleValue,
                lon = lonState.doubleValue,
                appid = API_KEY,
                units = unit
            )
        }
        LaunchedEffect(latState.doubleValue, lonState.doubleValue) {
            val weather = mainViewModel.getWeatherData(
                lat = latState.doubleValue,
                lon = lonState.doubleValue,
                appid = API_KEY,
                units = unit
            )
            weatherData.loading = false
            weatherData.data = weather.data
            weatherData.e = weather.e
        }
        if (weatherData.loading == true) {
            CircularProgressIndicator()
        } else if (weatherData.e != null) {
            Text(text = weatherData.e.toString())
        } else if (weatherData.data != null) {
            Column {
                MainScaffold(weather = weatherData.data!!, navController = navController, isImperial = isImperial)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(weather: Weather, navController: NavController, isImperial: Boolean) {

    val country = weather.sys.country

    Scaffold(topBar = {
        WeatherAppBar(
            title = (weather.name + ", " + country),
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
            elevation = 5.dp
        ) {

        }
    }
    ) {
        MainContent(data = weather, isImperial = isImperial)
    }
}

@Composable
fun MainContent(data: Weather, isImperial: Boolean) {
    val imageUrl = "$IMAGE_URL${data.weather[0].icon}.png"
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatDate(data.dt), // Wed Nov 30
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = formatDecimals(data.main.temp - 273) + "º",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = data.weather[0].main,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        HumidityWindPressureRow(
            main = data.main,
            wind = data.wind,
            isImperial = isImperial
        )
        Divider()
        SunsetSunRiseRow(sys = data.sys)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(size = 14.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                items(data.weather) { item ->
                    WeatherDetailRow(weather = data, weatherX = item, main = data.main)
                }
            }
        }
    }
}