package com.example.weatherchecker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_records")
data class WeatherRecordData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val location: String,
    val dateTime: Long, // Storing as a timestamp for flexibility
    val temperature: Double
)