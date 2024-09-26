package com.aidannemeth.weatherly.feature.weather.data.repository.mapper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
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

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<StoreReadResponse<Weather>>.mapToEither(): Flow<Either<DataError, Weather>> =
    transformLatest { response ->
        when (response) {
            is Data -> emit(response.dataToEither())
            is Error.Custom<*> -> TODO()
            is Error.Exception -> emit(response.exceptionToEither())
            is Error.Message -> TODO()
            Initial -> TODO()
            is Loading -> Unit
            is NoNewData -> TODO()
        }
}

fun Data<Weather>.dataToEither() =
    dataOrNull()?.right() ?: toDataError().left()

private fun Data<Weather>.toDataError() = when (origin) {
    Cache,
    SourceOfTruth -> DataError.Local.NoCachedData

    is Fetcher -> DataError.Remote.NoNewData
    StoreReadResponseOrigin.Initial -> DataError.Unknown
}

private fun Error.Exception.exceptionToEither() = when (origin) {
    Cache -> TODO()
    SourceOfTruth -> localExceptionToEither()
    is Fetcher -> remoteExceptionToEither()
    StoreReadResponseOrigin.Initial -> TODO()
}

private fun Error.Exception.localExceptionToEither() =
    when (error) {
        is OutOfMemoryError -> DataError.Local.OutOfMemory
        else -> DataError.Local.Unknown
    }.left()

private fun Error.Exception.remoteExceptionToEither() =
    error.toHttpDataError().left()
