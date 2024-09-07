package com.aidannemeth.weatherly.feature.weather.data.local

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toEntity
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val db: WeatherDatabase,
) : WeatherLocalDataSource {
    override suspend fun insertWeather(weather: Weather): Either<DataError.Local, Unit> =
        Either.catch { db.weatherDao().insert(weather.toEntity()) }
            .mapLeft { DataError.Local.DbWriteFailed }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWeather(): Flow<Either<DataError.Local, Weather>> =
        db.weatherDao().observe()
            .mapLatest { entity ->
                entity?.toWeather()?.right() ?: NoCachedData.left()
            }
}
