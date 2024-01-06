package com.example.jetweatherforcast.data


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetweatherforcast.model.Unit
import com.example.jetweatherforcast.model_city.Favorite

@Database(
    entities = [Favorite::class, Unit::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

}