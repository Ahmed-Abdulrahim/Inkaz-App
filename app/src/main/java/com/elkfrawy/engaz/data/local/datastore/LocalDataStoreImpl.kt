package com.elkfrawy.engaz.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalDataStoreImpl @Inject constructor(val dataStore: DataStore<Preferences>): LocalDataStore {

    override suspend fun saveInt(key: String, value: Long) {
        val pKey = longPreferencesKey(key)
        dataStore.edit {
            it[pKey] = value
        }
    }

    override suspend fun loadInt(key: String): Long {
        val preferences = dataStore.data.first()
        return preferences[longPreferencesKey(key)] ?: -1
    }

    override suspend fun saveString(key: String, value: String) {
        val pKey = stringPreferencesKey(key)
        dataStore.edit {
            it[pKey] = value
        }
    }

    override suspend fun loadString(key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        val pKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[pKey] = value
        }
    }

    override suspend fun loadBoolean(key: String): Boolean {
        val preferences = dataStore.data.first()
        return preferences[booleanPreferencesKey(key)] ?: false
    }
}