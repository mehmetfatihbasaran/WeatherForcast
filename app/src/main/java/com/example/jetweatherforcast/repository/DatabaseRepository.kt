package com.example.jetweatherforcast.repository

import com.example.jetweatherforcast.data.WeatherDao
import com.example.jetweatherforcast.model.Unit
import com.example.jetweatherforcast.model_city.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao: WeatherDao) {

    fun getFavorites(): Flow<List<Favorite>> = dao.getFavorites()
    suspend fun insertFavorite(favorite: Favorite) = dao.insertFavorite(favorite)
    suspend fun updateFavorite(favorite: Favorite) = dao.updateFavorite(favorite)
    suspend fun deleteAllFavorites() = dao.deleteAllFavorites()
    suspend fun deleteFavorite(favorite: Favorite) = dao.deleteFavorite(favorite)
    suspend fun getFavById(city: String): Favorite = dao.getFavoriteByCity(city)

    fun getUnits(): Flow<List<Unit>> = dao.getUnits()
    suspend fun insertUnit(unit: Unit) = dao.insertUnit(unit)
    suspend fun updateUnit(unit: Unit) = dao.updateUnit(unit)
    suspend fun deleteAllUnits() = dao.deleteAllUnits()
    suspend fun deleteUnit(unit: Unit) = dao.deleteUnit(unit)

}