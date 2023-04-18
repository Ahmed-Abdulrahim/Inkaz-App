package com.elkfrawy.engaz.domain.usecases.rate

import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.repository.HistoryRepository
import com.elkfrawy.engaz.domain.repository.RateRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class GetRate @Inject constructor(val rateRepository: RateRepository) {

    suspend fun execute(user_id: Long): Result<List<Rate>> = rateRepository.getRate(user_id)
}