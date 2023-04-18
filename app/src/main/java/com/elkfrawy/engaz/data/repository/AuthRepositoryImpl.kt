package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.remote.user.AuthRemoteSource
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(val autRemoteSource: AuthRemoteSource): AuthRepository {
    override suspend fun addUser(
        name: String,
        number: Long,
        nationalID: String,
        password: String,
        address: String,
        car_name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String
    ): Result<Long> = autRemoteSource.addUser(name, number, nationalID, password, address, car_name, car_model, car_number, car_color, car_license)

    override suspend fun login(number: Long, password: String): Result<Long> = autRemoteSource.login(number, password)

    override suspend fun show(id: Long): Result<User> = autRemoteSource.show(id)

    override suspend fun updateEmail(id: Long, email: String): Result<Unit> = autRemoteSource.updateEmail(id, email)

    override suspend fun updateMobile(id: Long, mobile: Long): Result<Unit> = autRemoteSource.updateMobile(id, mobile)

    override suspend fun updatePassword(id: Long, password: String): Result<Unit> = autRemoteSource.updatePassword(id, password)

    override suspend fun updateLatLng(id: Long, lat: String, lng: String): Result<Unit> = autRemoteSource.updateLatLng(id, lat, lng)
    override suspend fun updateUserInfo(
        id: Long,
        name: String,
        address: String,
        nationalID: String
    ): Result<Unit> = autRemoteSource.updateUserInfo(id, name, address, nationalID)
}