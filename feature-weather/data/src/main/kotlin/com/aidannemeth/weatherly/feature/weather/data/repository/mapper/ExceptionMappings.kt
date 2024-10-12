package com.aidannemeth.weatherly.feature.weather.data.repository.mapper

import com.aidannemeth.weatherly.feature.common.domain.mapper.fromHttpCode
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(ExperimentalSerializationApi::class)
fun Throwable.toHttpDataError(): DataError.Remote = when (this) {
    is HttpException -> DataError.Remote.Http(
        networkError = NetworkError.fromHttpCode(code()),
        apiErrorInfo = message,
    )

    is MissingFieldException -> DataError.Remote.Http(
        networkError = NetworkError.Parse,
        apiErrorInfo = message,
    )

    is UnknownHostException,
    is ProtocolException,
    is SocketTimeoutException
    -> DataError.Remote.Http(
        networkError = NetworkError.NoNetwork,
        apiErrorInfo = message,
    )

    else -> {
        Timber.w("Unknown exception: $message", this.message)
        DataError.Remote.Unknown
    }
}
