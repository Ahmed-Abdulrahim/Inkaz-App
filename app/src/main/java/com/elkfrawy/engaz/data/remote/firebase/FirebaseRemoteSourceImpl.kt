package com.elkfrawy.engaz.data.remote.firebase

import com.elkfrawy.engaz.data.remote.Api
import com.elkfrawy.engaz.data.util.ClientException
import com.elkfrawy.engaz.data.util.DataNotAvailableException
import com.elkfrawy.engaz.data.util.ServerException
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.util.Result
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRemoteSourceImpl @Inject constructor(val api: Api) : FirebaseRemoteSource {

    override suspend fun sendNotification(notificationResponse: NotificationResponse): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.sendNotification(notificationResponse)

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