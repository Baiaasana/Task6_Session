package com.example.wrlpages

import android.content.Context
import android.provider.ContactsContract.DisplayNameSources.EMAIL
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object MyDataStore {

    private val EMAIL = "email"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = EMAIL)

    suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        App.context?.dataStore?.edit { email ->
            email[dataStoreKey] = value
        }
    }

     fun read(key: String): Flow<String>? {
        val dataStoreKey = stringPreferencesKey(key)
        return App.context?.dataStore?.data?.map {
            it[dataStoreKey] ?: ""
        }
    }
}