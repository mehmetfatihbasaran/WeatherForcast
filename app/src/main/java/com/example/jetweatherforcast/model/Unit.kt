package com.example.jetweatherforcast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "settings_table")
data class Unit(
    @PrimaryKey
    @ColumnInfo(name = "unit")
    val unit: String
)
