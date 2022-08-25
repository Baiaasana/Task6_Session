package com.example.wrlpages.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.example.wrlpages.utils.UserDataStore

class HomeViewModel : ViewModel() {

    fun getPreferences() = UserDataStore.getPreferences()

    suspend fun remove(key:String) {
        UserDataStore.remove(key)
    }

}