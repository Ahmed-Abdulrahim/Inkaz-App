package com.elkfrawy.engaz.domain.usecases.history

import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.repository.HistoryRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class GetHistory @Inject constructor(val historyRepository: HistoryRepository) {

    suspend fun execute(user_id: Long): Result<List<History>> = historyRepository.getHistory(user_id)
}