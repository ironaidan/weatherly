package com.aidannemeth.weatherly.feature.common.domain.model

sealed interface DataError {

    sealed interface Local : DataError {

        data object NoCachedData : Local

        data object OutOfMemory : Local

        data object Unknown : Local
    }

    sealed interface Remote : DataError {

        data class Http(
            val networkError: NetworkError,
            val apiErrorInfo: String? = null,
        ) : Remote

        data object NoNewData : Remote

        data object Unknown : Remote
    }

    data object Unknown : DataError
}
