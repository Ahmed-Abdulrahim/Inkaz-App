package com.elkfrawy.engaz.domain.usecases

import android.location.Location
import com.elkfrawy.engaz.domain.location.LocationClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocation @Inject constructor(val locationClient: LocationClient) {

    fun getLocation(): Flow<Location> = locationClient.getLocationUpdates(500)

}