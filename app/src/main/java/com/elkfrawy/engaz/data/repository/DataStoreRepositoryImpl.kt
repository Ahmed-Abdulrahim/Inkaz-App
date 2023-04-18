package com.elkfrawy.engaz.data.repository

import com.elkfrawy.engaz.data.local.datastore.LocalDataStore
import com.elkfrawy.engaz.domain.repository.DataStoreRepository
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(val localDataStore: LocalDataStore): DataStoreRepository {

    override suspend fun saveString(key: String, value: String) = localDataStore.saveString(key, value)
    override suspend fun loadString(key: String): String = localDataStore.loadString(key)
    override suspend fun loadBoolean(key: String): Boolean = localDataStore.loadBoolean(key)
    override suspend fun saveBoolean(key: String, value: Boolean) = localDataStore.saveBoolean(key, value)
    override suspend fun saveInt(key: String, value: Long) = localDataStore.saveInt(key, value)
    override suspend fun loadInt(key: String): Long = localDataStore.loadInt(key)
}