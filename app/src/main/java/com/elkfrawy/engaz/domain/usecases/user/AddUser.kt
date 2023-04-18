package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.User
import  com.elkfrawy.engaz.domain.util.Result
import com.elkfrawy.engaz.domain.repository.AuthRepository
import javax.inject.Inject

class AddUser @Inject constructor(val authRepo: AuthRepository) {

    suspend fun execute(
        name: String,
        number: Long,
        nationalID: String,
        password: String,
        address:String,
        car_name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String,
    ): Result<Long> =
        authRepo.addUser(name, number, nationalID, password, address, car_name, car_model, car_number, car_color, car_license)

}