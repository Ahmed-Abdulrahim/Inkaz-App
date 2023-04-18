package com.elkfrawy.engaz.data.remote.user

import com.elkfrawy.engaz.domain.util.Result
import com.elkfrawy.engaz.domain.model.User

interface AuthRemoteSource {

    suspend fun addUser(
        name: String, number: Long,
        nationalID: String,
        password: String, address: String,
        car_name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String,
    ): Result<Long>

    suspend fun login(number: Long, password: String): Result<Long>
    suspend fun show(id: Long): Result<User>
    suspend fun updateUserInfo(id: Long, name: String, address: String, nationalID: String):Result<Unit>
    suspend fun updateEmail(id: Long, email: String): Result<Unit>
    suspend fun updateMobile(id: Long, mobile: Long): Result<Unit>
    suspend fun updatePassword(id: Long, password: String): Result<Unit>
    suspend fun updateLatLng(id: Long, lat: String, lng: String): Result<Unit>


}