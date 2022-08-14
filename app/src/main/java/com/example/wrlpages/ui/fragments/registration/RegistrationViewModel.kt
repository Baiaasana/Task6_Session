package com.example.wrlpages.ui.fragments.registration

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrlpages.models.register.RegisterDataModel
import com.example.wrlpages.models.register.RegisterModel
import com.example.wrlpages.network.RetrofitClient
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<RegisterModel>>(Resource.Success())
    val registerState = _registerState.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            registerResponse(email = email, password = password).collect {
                _registerState.value = it
            }
        }
    }

    private fun registerResponse(email: String, password: String) = flow {
        emit(Resource.Loader(true))
        try {
            val response = RetrofitClient.authService.register(RegisterDataModel(email, password))
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    d("data", "$body")
                    emit(Resource.Success(body))
                }
                else -> {
                    val errorBody = response.errorBody()
                    d("data", "$errorBody")
                    emit(Resource.Error(errorBody?.string() ?: "error"))
                }
            }
        } catch (e: Throwable) {
            emit(Resource.Error(message = e.message ?: "error"))
        }
    }
}