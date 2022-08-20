package com.iurysouza.livematch.data.models

data class NetworkResponse<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T): NetworkResponse<T> {
            return NetworkResponse(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: Throwable?, data: T?=null): NetworkResponse<T> {
            return NetworkResponse(
                Status.ERROR,
                data,
                error
            )
        }
    }

    fun isSuccess() = status == Status.SUCCESS
    fun isError() = status == Status.ERROR
    enum class Status {
        SUCCESS,
        ERROR,
    }
}
