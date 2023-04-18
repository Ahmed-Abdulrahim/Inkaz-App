package com.elkfrawy.engaz.domain.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationClientImpl @Inject constructor (@ApplicationContext val context: Context, val fused: FusedLocationProviderClient): LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {

       return callbackFlow {
           val request = LocationRequest.Builder(interval)
               .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
               .build()

           val locationCallback = object : LocationCallback(){
               override fun onLocationResult(p0: LocationResult) {
                   super.onLocationResult(p0)
                   p0.locations.lastOrNull()?.let {
                       launch { send(it) }
                   }
               }
           }

           fused.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

           awaitClose{
               fused.removeLocationUpdates(locationCallback)
           }
       }
    }
}