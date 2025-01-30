package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample

class WeatherRemoteDataSourceTestFake : WeatherRemoteDataSource {
    var weather = WeatherSample.buildIncreasedTemperature()

    override suspend fun getWeather(): Weather =
        weather
}
