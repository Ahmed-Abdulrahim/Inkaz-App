package com.elkfrawy.engaz.domain.repository

import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.util.Result

interface HistoryRepository {
    suspend fun getHistory(user_id: Long): Result<List<History>>

}