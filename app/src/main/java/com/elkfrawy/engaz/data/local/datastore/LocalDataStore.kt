package com.elkfrawy.engaz.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface LocalDataStore{

    suspend fun saveString(key: String, value: String)
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun loadString(key: String): String
    suspend fun loadBoolean(key: String): Boolean
    suspend fun saveInt(key: String, value: Long)
    suspend fun loadInt(key: String): Long

}