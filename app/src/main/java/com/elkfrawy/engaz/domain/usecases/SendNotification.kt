package com.elkfrawy.engaz.domain.usecases

import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.repository.FirebaseRepository
import javax.inject.Inject
import com.elkfrawy.engaz.domain.util.Result

class SendNotification @Inject constructor(val firebaseRepository: FirebaseRepository) {

    suspend fun execute(notificationResponse: NotificationResponse):Result<Unit>{
        return firebaseRepository.show(notificationResponse)
    }

}