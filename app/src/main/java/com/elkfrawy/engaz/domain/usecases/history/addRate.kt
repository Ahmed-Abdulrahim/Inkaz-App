package com.elkfrawy.engaz.domain.usecases.history

import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.repository.HistoryRepository
import com.elkfrawy.engaz.domain.repository.RateRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class addRate @Inject constructor(val rateRepository: RateRepository) {

    suspend fun execute(
        user_id: Long,
        user_id2: Long,
        rating: Int,
        message: String
    ): Result<Unit> =
        rateRepository.addRate(user_id, user_id2, rating, message)
}