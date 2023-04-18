package com.elkfrawy.engaz.domain.usecases.datastoreUsecases

import com.elkfrawy.engaz.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveInt @Inject constructor(val dataStoreRepository: DataStoreRepository) {
    suspend fun execute(key: String, value: Long) =  dataStoreRepository.saveInt(key, value)
}