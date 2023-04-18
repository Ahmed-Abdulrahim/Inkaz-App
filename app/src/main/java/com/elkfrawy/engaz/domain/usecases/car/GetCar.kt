package com.elkfrawy.engaz.domain.usecases.car
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.User
import  com.elkfrawy.engaz.domain.util.Result
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.repository.CarRepository
import javax.inject.Inject

class GetCar @Inject constructor(val carAuth: CarRepository) {

    suspend fun execute(user_id: Long):Result<List<Car>> = carAuth.getCar(user_id)

}