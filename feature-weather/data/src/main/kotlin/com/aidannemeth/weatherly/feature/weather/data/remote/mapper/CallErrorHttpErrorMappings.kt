package com.aidannemeth.weatherly.feature.weather.data.remote.mapper

import arrow.retrofit.adapter.either.networkhandling.CallError
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import com.aidannemeth.weatherly.feature.common.domain.mapper.fromHttpCode
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError

fun toHttpError(callError: CallError) =
    when (callError) {
        is HttpError -> DataError.Remote.Http(
            networkError = NetworkError.fromHttpCode(callError.code),
            apiErrorInfo = callError.message,
            isRetryable = false,
        )

        is IOError -> DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = callError.cause.message,
            isRetryable = true,
        )

        is UnexpectedCallError -> DataError.Remote.Http(
            networkError = NetworkError.Unknown,
            apiErrorInfo = callError.cause.message,
            isRetryable = false,
        )
    }
