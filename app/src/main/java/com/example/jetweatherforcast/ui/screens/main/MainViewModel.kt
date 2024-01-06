package com.example.jetweatherforcast.ui.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetweatherforcast.data.DataOrException
import com.example.jetweatherforcast.model.Weather
import com.example.jetweatherforcast.model_city.OpenCageResponse
import com.example.jetweatherforcast.repository.WeatherRepository
import com.example.jetweatherforcast.utils.Constants.MAP_API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun getWeatherData(
        lat: Double,
        lon: Double,
        appid: String,
        units: String
    ): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(lat, lon, appid, units)
    }

    private val _coordinates = MutableLiveData<OpenCageResponse>()
    val coordinates: LiveData<OpenCageResponse> get() = _coordinates
    fun getCoordinates(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                val result = repository.getCoordinates(apiKey = apiKey, cityName = query)
                _coordinates.value = result.data
            } catch (e: Exception) {
                // Hata durumuyla başa çıkabilirsiniz
                e.printStackTrace()
            }
        }
    }

    suspend fun getCityCoordinates(cityName: String): DataOrException<OpenCageResponse, Boolean, Exception> {
        Log.d("MainViewModel", "getCityCoordinates: $cityName")
        return repository.getCoordinates(
            apiKey = MAP_API_KEY,
            cityName = cityName
        )
    }


//    suspend fun getCoordinates(
//        cityName: String,
//        apiKey: String
//    ): DataOrException<Weather, Boolean, Exception> {
//        return repository.getCoordinates(cityName, apiKey)
//    }
//

//
//    fun getCityCoordinates(cityName: String) {
//        viewModelScope.launch {
//            try {
//                val result = repository.getCoordinates(cityName, API_KEY)
//                _weatherData.value = result
//            } catch (e: Exception) {
//                _weatherData.value = DataOrException(e = e)
//            }
//        }
//    }
}
