package com.elkfrawy.engaz.domain.repository

import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.util.Result

interface FirebaseRepository {

    suspend fun show(notificationResponse: NotificationResponse): Result<Unit>

}