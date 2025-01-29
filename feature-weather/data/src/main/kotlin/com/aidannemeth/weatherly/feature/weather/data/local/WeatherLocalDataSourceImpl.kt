package com.aidannemeth.weatherly.feature.weather.data.local

import com.aidannemeth.weatherly.feature.common.domain.coroutines.DefaultDispatcher
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toEntity
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val db: WeatherDatabase,
) : WeatherLocalDataSource {
    override suspend fun deleteAll() =
        db.weatherDao().deleteAll()

    override suspend fun insertWeather(weather: Weather) =
        withContext(dispatcher) {
            db.weatherDao().insert(weather.toEntity())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWeather(): Flow<Weather?> =
        db.weatherDao().observe()
            .mapLatest { entity -> entity?.toWeather() }
            .flowOn(dispatcher)
}
