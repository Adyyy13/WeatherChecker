package com.example.weatherchecker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationHelper(private val activity: Activity) {
    private val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun getLocation(): Location? {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return null
        }

        val isGPSEnabled: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGPSEnabled) {
            val locationListener = object : LocationListener {
                override fun onLocationChanged(locations: MutableList<Location>) {}

                override fun onLocationChanged(p0: Location) {}

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener)

            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        return null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 60000 // 1 minute
    }
}
