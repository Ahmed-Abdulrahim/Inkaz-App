package com.elkfrawy.engaz.domain.usecases.car
import com.elkfrawy.engaz.domain.repository.CarRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class UpdateCar @Inject constructor(val carAuth: CarRepository) {

    suspend fun execute(name: String, car_model: Int,
                        car_number: Int, car_color: String,
                        car_license: String, user_id: Long): Result<Unit> = carAuth.updateCarInfo(user_id, name, car_model, car_number, car_color, car_license)

}