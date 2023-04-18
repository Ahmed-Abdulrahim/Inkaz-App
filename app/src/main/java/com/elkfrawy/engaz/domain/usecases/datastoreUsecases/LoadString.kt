package com.elkfrawy.engaz.domain.usecases.datastoreUsecases

import com.elkfrawy.engaz.domain.repository.DataStoreRepository
import javax.inject.Inject

class LoadString @Inject constructor(val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(key: String): String =  dataStoreRepository.loadString(key)
}