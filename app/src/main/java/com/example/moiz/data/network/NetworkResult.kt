package com.example.moiz.data.network

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : NetworkResult<T>()
    data class Failure(val errorMessage : String) : NetworkResult<Nothing>()
}