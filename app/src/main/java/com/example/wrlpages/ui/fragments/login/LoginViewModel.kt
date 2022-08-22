package com.example.wrlpages.ui.fragments.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrlpages.models.login.LoginDataModel
import com.example.wrlpages.models.login.LoginModel
import com.example.wrlpages.network.RetrofitClient
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.wrlpages.App.Companion.context
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "email")

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginModel>>(Resource.Success())
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginResponse(email = email, password = password).collect {
                _loginState.value = it
            }

        }
    }

    private fun loginResponse(email: String, password: String) = flow {
        emit(Resource.Loader(true))
        try {
            val response =
                RetrofitClient.authService.login(LoginDataModel(email = email, password = password))
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    emit(Resource.Success(body))
                }
                else -> {
                    val errorBody = response.errorBody()
                    emit(Resource.Error(errorBody?.string() ?: "error"))
                }
            }
        } catch (e: Throwable) {
            emit(Resource.Error(message = e.message ?: "error"))
        }
    }

    suspend fun save(key:String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context?.dataStore?.edit { email ->
            email[dataStoreKey] = value
        }
    }

    suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context?.dataStore?.data?.first()
        return preferences?.get(dataStoreKey)
    }


}