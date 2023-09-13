package com.example.weatherchecker.database

import android.content.Context
import com.example.weatherchecker.database.AppDatabase
import com.example.weatherchecker.database.WeatherRecordDao
import com.example.weatherchecker.database.WeatherRecordData

class WeatherRepository(context: Context) {

    private val db: AppDatabase = AppDatabase.getDatabase(context)
    private val weatherDao: WeatherRecordDao = db.weatherRecordDao()

    suspend fun insertWeatherRecord(location: String, temperature: Double) {
        val record = WeatherRecordData(
            location = location,
            dateTime = System.currentTimeMillis(),
            temperature = temperature
        )
        weatherDao.insert(record)
    }

    suspend fun getAllWeatherRecords(): List<WeatherRecordData> {
        return weatherDao.getAllRecords()
    }

    suspend fun deleteWeatherRecord(record: WeatherRecordData) {
        weatherDao.delete(record)
    }
}
