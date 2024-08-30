package com.aidannemeth.weatherly.feature.weather.data.local

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherLocalDataSourceImpl @Inject constructor(
    private val db: WeatherDatabase,
) : WeatherLocalDataSource {

    override suspend fun getWeather(): Either<NoCachedData, Weather> =
        observeWeather().first()

    override fun observeWeather(): Flow<Either<NoCachedData, Weather>> =
        db.weatherDao().observe()
            .mapLatest { entity ->
                entity?.toWeather()?.right() ?: NoCachedData.left()
            }
}
