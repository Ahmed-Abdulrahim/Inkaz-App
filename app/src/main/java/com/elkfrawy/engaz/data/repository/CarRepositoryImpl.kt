package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.remote.car.CarRemoteSource
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.repository.CarRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(val carRemoteSource: CarRemoteSource): CarRepository {

    override suspend fun updateCarInfo(
        user_id: Long,
        name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String
    ): Result<Unit> = carRemoteSource.updateCarInfo(user_id, name, car_model, car_number, car_color, car_license)

    override suspend fun getCar(user_id: Long): Result<List<Car>> = carRemoteSource.getCar(user_id)
}