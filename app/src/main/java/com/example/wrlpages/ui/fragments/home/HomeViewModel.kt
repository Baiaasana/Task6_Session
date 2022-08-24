package com.example.wrlpages.ui.fragments.home

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrlpages.App
import com.example.wrlpages.MyDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    fun read(key: String) = flow{
        MyDataStore.read(key)?.collect{

            emit(it)

        }
    }




}