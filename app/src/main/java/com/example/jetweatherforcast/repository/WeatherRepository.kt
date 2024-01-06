package com.example.jetweatherforcast.repository

import android.util.Log
import com.example.jetweatherforcast.data.DataOrException
import com.example.jetweatherforcast.model.Weather
import com.example.jetweatherforcast.model_city.OpenCageResponse
import com.example.jetweatherforcast.network.MapApi
import com.example.jetweatherforcast.network.WeatherApi
import com.example.jetweatherforcast.utils.Constants.MAP_API_KEY
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val mapApi: MapApi
) {

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        appid: String,
        units: String
    ): DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(lat, lon, appid)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return if (response.isSuccessful) {
            DataOrException(data = response.body())
        } else {
            DataOrException(e = Exception("API request failed"))
        }
    }

    suspend fun getCoordinates(
        apiKey: String = MAP_API_KEY,
        cityName: String
    ): DataOrException<OpenCageResponse, Boolean, Exception> {
        val response = try {
            mapApi.getCoordinates(apiKey, cityName)
        } catch (e: Exception) {
            Log.d("MainRepository", "getCoordinates: $e")
            return DataOrException(e = e)
        }
        Log.d("MainRepository", "getCoordinates: $response")
        return if (response.isSuccessful) {
            DataOrException(data = response.body())
        } else {
            DataOrException(e = Exception("API request failed"))
        }
    }
}
