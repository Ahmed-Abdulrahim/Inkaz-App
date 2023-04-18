package com.elkfrawy.engaz.data.remote.car

import android.util.Log
import com.elkfrawy.engaz.data.remote.Api
import com.elkfrawy.engaz.data.util.ClientException
import com.elkfrawy.engaz.data.util.DataNotAvailableException
import com.elkfrawy.engaz.data.util.ServerException
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRemoteSourceImpl @Inject constructor(val api: Api) : CarRemoteSource {

    override suspend fun updateCarInfo(
        user_id: Long,
        name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String,

        ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response =
                api.updateCarInfo(user_id, name, car_model, car_number, car_color, car_license)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun getCar(user_id: Long): Result<List<Car>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getCar(user_id)

            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

}