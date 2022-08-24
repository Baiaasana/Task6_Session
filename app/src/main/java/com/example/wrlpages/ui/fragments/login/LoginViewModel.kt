package com.example.wrlpages.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrlpages.Constants
import com.example.wrlpages.MyDataStore
import com.example.wrlpages.models.login.LoginDataModel
import com.example.wrlpages.models.login.LoginModel
import com.example.wrlpages.network.RetrofitClient
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginModel>>(Resource.Success())
    val loginState = _loginState.asStateFlow()

    fun login(key: String, email: String, password: String) {
        viewModelScope.launch {
            loginResponse(key = key, email = email, password = password).collect {
                _loginState.value = it
            }
        }
    }

    private fun loginResponse(key: String, email: String, password: String) = flow {
        emit(Resource.Loader(true))
        try {
            val response =
                RetrofitClient.authService.login(LoginDataModel(email = email, password = password))
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    MyDataStore.save(key, email)
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



}