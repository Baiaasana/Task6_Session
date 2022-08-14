package com.example.wrlpages.network

import com.example.wrlpages.models.login.LoginDataModel
import com.example.wrlpages.models.login.LoginModel
import com.example.wrlpages.models.register.RegisterDataModel
import com.example.wrlpages.models.register.RegisterModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitClient {

    private const val BASE_URL = "https://reqres.in/api/"

    private val retrofitBuilder by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    val authService: AuthService = retrofitBuilder.create(AuthService::class.java)
}

interface AuthService {
    @POST("login")
    suspend fun login(@Body body: LoginDataModel): Response<LoginModel>

    @POST("register")
    suspend fun register(@Body body: RegisterDataModel): Response<RegisterModel>
}