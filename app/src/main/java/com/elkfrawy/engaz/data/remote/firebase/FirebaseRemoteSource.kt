package com.elkfrawy.engaz.data.remote.firebase

import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.util.Result

interface FirebaseRemoteSource {

    suspend fun sendNotification(notificationResponse: NotificationResponse): Result<Unit>

}