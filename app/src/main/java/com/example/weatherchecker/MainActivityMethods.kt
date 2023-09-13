package com.example.weatherchecker

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import java.util.Locale

class MainActivityMethods(private val mainActivity: MainActivity) {

    private fun getWeatherData(location: String, onWeatherReceived: (Double) -> Unit) {
        val url =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$location?unitGroup=metric&key=4XC97NYNVDUTTA86XHRYJNQ8L&contentType=json"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val firstDay = response.getJSONArray("days").getJSONObject(0)
                    val tempMax = firstDay.getDouble("tempmax")
                    onWeatherReceived(tempMax)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { _ ->
                Toast.makeText(mainActivity, "Error fetching weather data", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        MySingleton.getInstance(mainActivity).addToRequestQueue(jsonObjectRequest)
    }

    fun fetchWeatherData(location: String, onWeatherReceived: (Double) -> Unit) {
        getWeatherData(location, onWeatherReceived)
    }

    fun getCityName(latitude: Double, longitude: Double, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            @Suppress("DEPRECATION") val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].locality
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            "Unknown location"
        }
    }
}