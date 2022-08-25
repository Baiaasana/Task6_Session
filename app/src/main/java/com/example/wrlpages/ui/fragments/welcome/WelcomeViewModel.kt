package com.example.wrlpages.ui.fragments.welcome

import androidx.lifecycle.ViewModel
import com.example.wrlpages.utils.UserDataStore


class WelcomeViewModel : ViewModel() {

    fun getPreferences() = UserDataStore.getPreferences()


}