package com.elkfrawy.engaz.domain.usecases.datastoreUsecases

import com.elkfrawy.engaz.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveString @Inject constructor(val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(key: String, value: String) =  dataStoreRepository.saveString(key, value)
}