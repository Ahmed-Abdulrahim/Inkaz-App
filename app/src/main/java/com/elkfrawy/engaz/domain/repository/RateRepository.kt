package com.elkfrawy.engaz.domain.repository

import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.util.Result

interface RateRepository {

    suspend fun getRate(user_id: Long): Result<List<Rate>>
    suspend fun addRate(user_id: Long, user_id2: Long, rating: Int, message: String): Result<Unit>
}