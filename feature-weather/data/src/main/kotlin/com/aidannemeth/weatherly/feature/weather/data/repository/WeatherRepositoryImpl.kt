package com.aidannemeth.weatherly.feature.weather.data.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.coroutines.DefaultDispatcher
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.data.repository.mapper.mapToEither
import com.aidannemeth.weatherly.feature.weather.data.repository.mapper.toHttpDataError
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {
    private val weatherStore: Store<WeatherKey, Weather> =
        StoreBuilder.from(
            fetcher = Fetcher.of { _: WeatherKey ->
                weatherRemoteDataSource.getWeather()
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { _: WeatherKey ->
                    weatherLocalDataSource.observeWeather()
                },
                writer = { _: WeatherKey, weather: Weather ->
                    weatherLocalDataSource.insertWeather(weather)
                },
                deleteAll = {
                    weatherLocalDataSource.deleteAll()
                }
            )
        ).build()

    override fun observeWeather(): Flow<Either<DataError, Weather>> =
        weatherStore
            .stream(StoreReadRequest.cached(key = WeatherKey, refresh = true))
            .mapToEither()
            .flowOn(dispatcher)

    override suspend fun refreshWeather(): Either<DataError.Remote, Weather> =
        Either.catch {
            weatherStore.fresh(WeatherKey)
        }.mapLeft { exception: Throwable ->
            exception.toHttpDataError()
        }

    private object WeatherKey
}
