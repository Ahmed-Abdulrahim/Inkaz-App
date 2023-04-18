package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.remote.history.HistoryRemoteSource
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.repository.HistoryRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(val historyRemoteSource: HistoryRemoteSource): HistoryRepository {

    override suspend fun getHistory(user_id: Long): Result<List<History>> {
        return historyRemoteSource.getHistory(user_id)
    }
}