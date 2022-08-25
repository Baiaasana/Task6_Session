package com.example.wrlpages.utils

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wrlpages.App
import com.example.wrlpages.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

object UserDataStore {

    private val Application.store by preferencesDataStore(name = Constants.NAME)

    fun getPreferences(): Flow<Preferences> {
        return App.context.store.data
    }

    suspend fun save(key: String, value: String) {
        App.context.store.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun remove(key: String) {
        App.context.store.edit {
            it.remove(stringPreferencesKey(key))
        }
    }

    suspend fun clear(){
        App.context.store.edit {
            it.clear()
        }
    }

    suspend fun read(key: String): String? {
        return App.context.store.data.first()[stringPreferencesKey(key)]
    }

}