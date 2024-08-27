package com.aidannemeth.weatherly.feature.weather.data.local

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherLocalDataSourceImpl @Inject constructor(
    private val db: WeatherDatabase,
) : WeatherLocalDataSource {
    override suspend fun getWeather(): Weather? = observeWeather().firstOrNull()

    override fun observeWeather(): Flow<Weather?> = db.weatherDao().observe()
        .mapLatest { it?.toWeather() }
}
