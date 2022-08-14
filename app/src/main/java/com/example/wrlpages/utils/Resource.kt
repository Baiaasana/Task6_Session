package com.example.wrlpages.utils

sealed class Resource<T> {
    data class Success<T>(val data: T? = null) : Resource<T>()
    data class Error<T>(val message: String?) : Resource<T>()
    data class Loader<T>(val loading: Boolean) : Resource<T>()
}