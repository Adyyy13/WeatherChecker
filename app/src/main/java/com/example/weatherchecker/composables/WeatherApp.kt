package com.example.weatherchecker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherchecker.LocationHelper
import com.example.weatherchecker.MainActivity
import com.example.weatherchecker.MainActivityMethods
import com.example.weatherchecker.database.WeatherRepository
import com.example.weatherchecker.database.WeatherRecordData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherApp(mainActivity: MainActivity) {
    val repository by lazy { WeatherRepository(mainActivity) }
    val locationHelper by lazy { LocationHelper(mainActivity) }
    val methods = MainActivityMethods(mainActivity)
    val coroutineScope = rememberCoroutineScope()
    var weatherRecords by remember { mutableStateOf(listOf<WeatherRecordData>()) } // Note the 'var'

    // Load weather records from the database initially
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val allRecords = repository.getAllWeatherRecords()
            weatherRecords = allRecords // Assign the fetched records directly
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Check weather" button
        Button(onClick = {
            val location = locationHelper.getLocation()
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                val city = methods.getCityName(latitude, longitude, mainActivity)
                val locString = "${latitude},${longitude}"
                methods.fetchWeatherData(locString) { temperature ->
                    coroutineScope.launch {
                        repository.insertWeatherRecord(city, temperature)
                        val updatedRecords = repository.getAllWeatherRecords()
                        weatherRecords = updatedRecords // Assign the fetched records directly
                    }
                }
            }
        }) {
            Text("Check weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display Weather Records
        for (record in weatherRecords) {
            val dateString = SimpleDateFormat(
                "dd MMM, hh:mm a",
                Locale.getDefault()
            ).format(Date(record.dateTime))
            WeatherRecord(
                location = record.location,
                dateTime = dateString,
                temperature = "${record.temperature}°C"
            ) {
                // This is the lambda function which will be executed on clicking a record
                coroutineScope.launch {
                    repository.deleteWeatherRecord(record)
                    weatherRecords = weatherRecords.filter { it != record }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}