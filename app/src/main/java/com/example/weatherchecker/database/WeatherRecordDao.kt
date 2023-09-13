package com.example.weatherchecker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherRecordDao {
    @Insert
    suspend fun insert(record: WeatherRecordData)

    @Query("SELECT * FROM weather_records ORDER BY dateTime DESC")
    suspend fun getAllRecords(): List<WeatherRecordData>

    @Delete
    suspend fun delete(record: WeatherRecordData)
}