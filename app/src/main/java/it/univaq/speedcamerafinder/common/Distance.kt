package it.univaq.speedcamerafinder.common

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import it.univaq.speedcamerafinder.domain.model.SpeedCamera

// Distanza in metri tra la posizione dell'utente e l'autovelox
fun SpeedCamera.distanceFrom(location: LatLng): Float {
    val result = FloatArray(1)
    Location.distanceBetween(location.latitude, location.longitude, lat, lng, result)
    return result[0]
}
