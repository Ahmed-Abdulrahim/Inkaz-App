package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.remote.rate.RateRemoteSource
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.repository.RateRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(val rateRemoteSource: RateRemoteSource): RateRepository {

    override suspend fun getRate(user_id: Long): Result<List<Rate>> = rateRemoteSource.getRate(user_id)

    override suspend fun addRate(
        user_id: Long,
        user_id2: Long,
        rating: Int,
        message: String
    ): Result<Unit> = rateRemoteSource.addRate(user_id, user_id2, rating, message)
}