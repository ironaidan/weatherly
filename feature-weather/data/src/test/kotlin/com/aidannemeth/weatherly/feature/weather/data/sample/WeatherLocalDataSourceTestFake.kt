package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WeatherLocalDataSourceTestFake : WeatherLocalDataSource {
    private val weather: Weather = WeatherSample.build()
    var weatherFlow = MutableStateFlow<Weather?>(weather)
    override suspend fun insertWeather(weather: Weather) {
        weatherFlow.value = weather
    }

    override fun observeWeather(): Flow<Weather?> =
        weatherFlow
}
