package com.elkfrawy.engaz.domain.usecases.datastoreUsecases

import com.elkfrawy.engaz.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveBoolean @Inject constructor(val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(key: String, value: Boolean) =  dataStoreRepository.saveBoolean(key, value)
}