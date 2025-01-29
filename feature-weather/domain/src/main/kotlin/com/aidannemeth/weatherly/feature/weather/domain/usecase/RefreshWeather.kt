package com.aidannemeth.weatherly.feature.weather.domain.usecase

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class RefreshWeather @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(): Either<DataError.Remote, Weather> =
        weatherRepository.refreshWeather()
}
