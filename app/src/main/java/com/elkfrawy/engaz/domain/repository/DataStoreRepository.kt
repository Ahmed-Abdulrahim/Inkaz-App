package com.elkfrawy.engaz.domain.repository

interface DataStoreRepository {

    suspend fun saveString(key: String, value: String)
    suspend fun loadString(key: String): String
    suspend fun loadBoolean(key: String): Boolean
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun saveInt(key: String, value: Long)
    suspend fun loadInt(key: String): Long

}