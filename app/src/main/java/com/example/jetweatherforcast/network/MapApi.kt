package com.example.jetweatherforcast.network

import com.example.jetweatherforcast.model_city.OpenCageResponse
import com.example.jetweatherforcast.utils.Constants.MAP_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MapApi {
    @GET("geocode/v1/json")
    suspend fun getCoordinates(
        @Query("key") key: String = MAP_API_KEY ?: "eaccb710eadf4cdb89826372a16db56b",
        @Query("q") cityName: String
    ): Response<OpenCageResponse>
}
