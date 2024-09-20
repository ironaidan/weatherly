package com.aidannemeth.weatherly.feature.weather.data.repository.mapper

import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.mapper.fromHttpCode
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponse.Data
import org.mobilenativefoundation.store.store5.StoreReadResponse.Error
import org.mobilenativefoundation.store.store5.StoreReadResponse.Initial
import org.mobilenativefoundation.store.store5.StoreReadResponse.Loading
import org.mobilenativefoundation.store.store5.StoreReadResponse.NoNewData
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin.Cache
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin.Fetcher
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin.SourceOfTruth
import retrofit2.HttpException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun StoreReadResponse<Weather>.toEither() = when (this) {
    is Data -> responseDataToEither()
    is Error.Custom<*> -> TODO()
    is Error.Exception -> exceptionToEither()
    is Error.Message -> TODO()
    Initial -> TODO()
    is Loading -> loadingToEither()
    is NoNewData -> TODO()
}

fun Data<Weather>.responseDataToEither() =
    dataOrNull()?.right() ?: toDataError().left()

// This can be erroneously called on a right
private fun Data<Weather>.toDataError() = when (origin) {
    Cache,
    SourceOfTruth -> DataError.Local.NoCachedData

    is Fetcher -> DataError.Remote.NoNewData
    StoreReadResponseOrigin.Initial -> DataError.Unknown
}

private fun Error.Exception.exceptionToEither() = when (origin) {
    Cache -> TODO()
    SourceOfTruth -> {
        when (error) {
            is OutOfMemoryError -> DataError.Local.OutOfMemory.left()
            else -> DataError.Local.Unknown.left()
        }
    }
    is Fetcher -> remoteExceptionToEither()
    StoreReadResponseOrigin.Initial -> TODO()
}

// This can be called on local exceptions also, which is not ideal.
@OptIn(ExperimentalSerializationApi::class)
private fun Error.Exception.remoteExceptionToEither() =
    when (val exception = error) {
        is HttpException -> DataError.Remote.Http(
            networkError = NetworkError.fromHttpCode(exception.code()),
            apiErrorInfo = exception.message,
        )

        is UnknownHostException -> DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = exception.message,
        )

        is MissingFieldException -> DataError.Remote.Http(
            networkError = NetworkError.Parse,
            apiErrorInfo = exception.message,
        )

        is ProtocolException,
        is SocketTimeoutException
        -> DataError.Remote.Http(
            networkError = NetworkError.NoNetwork,
            apiErrorInfo = exception.message,
        )

        else -> TODO()
    }.left()

fun Loading.loadingToEither() = when (origin) {
    Cache,
    SourceOfTruth -> DataError.Local.Loading

    is Fetcher -> DataError.Remote.Loading
    StoreReadResponseOrigin.Initial -> TODO()
}.left()
