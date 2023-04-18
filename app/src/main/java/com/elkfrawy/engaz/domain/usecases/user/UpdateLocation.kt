package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class UpdateLocation @Inject constructor(val authRepo: AuthRepository) {

    suspend fun execute(id: Long, lat: String, lng: String):Result<Unit> = authRepo.updateLatLng(id, lat, lng)
}