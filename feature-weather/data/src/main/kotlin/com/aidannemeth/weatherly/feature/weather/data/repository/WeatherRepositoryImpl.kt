package com.aidannemeth.weatherly.feature.weather.data.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.data.repository.mapper.toEither
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWeather(): Flow<Either<DataError, Weather>> =
        buildWeatherStore()
            .stream(StoreReadRequest.cached(key = Unit, refresh = true))
            .mapLatest { response -> response.toEither() }

    private fun buildWeatherStore(): Store<Any, Weather> =
        StoreBuilder.from(
            fetcher = Fetcher.of { weatherRemoteDataSource.getWeather() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { weatherLocalDataSource.observeWeather() },
                writer = { _, weather -> weatherLocalDataSource.insertWeather(weather) },
            )
        ).build()
}
