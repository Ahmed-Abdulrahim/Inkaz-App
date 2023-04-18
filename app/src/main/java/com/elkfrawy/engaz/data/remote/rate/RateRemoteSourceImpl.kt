package com.elkfrawy.engaz.data.remote.rate

import android.util.Log
import com.elkfrawy.engaz.data.remote.Api
import com.elkfrawy.engaz.data.util.ClientException
import com.elkfrawy.engaz.data.util.DataNotAvailableException
import com.elkfrawy.engaz.data.util.ServerException
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RateRemoteSourceImpl @Inject constructor(val api: Api) : RateRemoteSource {


    override suspend fun getRate(user_id: Long): Result<List<Rate>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = api.getRating(user_id)
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

    override suspend fun addRate(user_id: Long, user_id2: Long, rating: Int, message: String):Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = api.insertRating(user_id, user_id, rating, message)
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

    suspend fun getHistory(user_id: Long): Result<List<History>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = api.getHistory(user_id)
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