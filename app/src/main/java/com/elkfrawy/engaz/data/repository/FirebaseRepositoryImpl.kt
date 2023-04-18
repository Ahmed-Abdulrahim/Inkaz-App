package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.remote.firebase.FirebaseRemoteSource
import com.elkfrawy.engaz.data.remote.history.HistoryRemoteSource
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.repository.FirebaseRepository
import com.elkfrawy.engaz.domain.repository.HistoryRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(val firebaseRemoteSource: FirebaseRemoteSource): FirebaseRepository {

    override suspend fun show(notificationResponse: NotificationResponse): Result<Unit> {
        return firebaseRemoteSource.sendNotification(notificationResponse)
    }
}