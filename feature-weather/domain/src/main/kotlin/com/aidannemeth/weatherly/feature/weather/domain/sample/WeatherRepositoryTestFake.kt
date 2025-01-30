package com.aidannemeth.weatherly.feature.weather.domain.sample

import arrow.core.Either
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryTestFake : WeatherRepository {
    var observeWeatherResult: Either<DataError, Weather> =
        WeatherSample.build().right()

    var refreshWeatherResult: Either<DataError.Remote, Weather> =
        WeatherSample.buildIncreasedTemperature().right()

    override fun observeWeather(): Flow<Either<DataError, Weather>> {
        return flowOf(observeWeatherResult)
    }

    override suspend fun refreshWeather(): Either<DataError.Remote, Weather> {
        return refreshWeatherResult
    }
}
