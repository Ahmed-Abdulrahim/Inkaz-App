package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.User
import  com.elkfrawy.engaz.domain.util.Result
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.repository.CarRepository
import javax.inject.Inject

class UpdateUserInfo @Inject constructor(val authRepository: AuthRepository) {

    suspend fun execute(id: Long, name: String, address: String, nationalID: String):Result<Unit> =
        authRepository.updateUserInfo(id, name, address, nationalID)


}