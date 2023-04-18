package com.elkfrawy.engaz.data.remote.history

import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.util.Result

interface HistoryRemoteSource {

    suspend fun getHistory(user_id: Long):Result<List<History>>

}