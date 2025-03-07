package com.dgnt.movienensemble.core.util

sealed class Resource<T>(val data: T? = null) {
    data class Loading<T>(private val d: T? = null) : Resource<T>(d)
    data class Success<T>(private val d: T?) : Resource<T>(d)
    sealed class Error<T>(data: T? = null, val exception: Exception? = null) : Resource<T>(data) {
        data class HttpError<T>(private val d: T? = null, val e: Exception? = null) : Error<T>(d, e)
        data class IOError<T>(private val d: T? = null, val e: Exception? = null) : Error<T>(d, e)
    }
}