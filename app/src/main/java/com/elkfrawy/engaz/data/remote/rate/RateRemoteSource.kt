package com.elkfrawy.engaz.data.remote.rate

import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.util.Result

interface RateRemoteSource {

    suspend fun getRate(user_id: Long):Result<List<Rate>>
    suspend fun addRate(user_id: Long, user_id2: Long, rating: Int, message: String):Result<Unit>
}