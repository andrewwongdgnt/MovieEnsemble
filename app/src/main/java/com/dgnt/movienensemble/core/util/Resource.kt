package com.dgnt.movienensemble.core.util

sealed class Resource<T>(val data: T? = null) {
    data class Loading<T>(private val loadingData: T? = null) : Resource<T>(loadingData)
    data class Success<T>(private val successData: T?) : Resource<T>(successData)
    sealed class Error<T>(data: T? = null, val exception: Exception? = null) : Resource<T>(data) {
        data class HttpError<T>(private val exData: T? = null, val ex: Exception? = null) : Error<T>(exData, ex)
        data class IOError<T>(private val exData: T? = null, val ex: Exception? = null) : Error<T>(exData, ex)
    }
}