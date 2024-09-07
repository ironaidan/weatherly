package com.aidannemeth.weatherly.feature.weather.data.local

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
    override suspend fun insertWeather(weather: Weather) =
        db.weatherDao().insert(weather.toEntity())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWeather(): Flow<Weather?> = db.weatherDao().observe()
        .mapLatest { entity -> entity?.toWeather() }
}
