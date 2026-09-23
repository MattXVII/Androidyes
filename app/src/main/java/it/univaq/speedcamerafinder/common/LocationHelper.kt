package it.univaq.speedcamerafinder.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper (context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.Builder(0)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    // Il permesso è già stato concesso: start() viene chiamato solo dentro PermissionGate
    @SuppressLint("MissingPermission")
    fun start(callback: LocationCallback) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, callback, Looper.getMainLooper())
    }

    fun stop(callback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(callback)
    }
}