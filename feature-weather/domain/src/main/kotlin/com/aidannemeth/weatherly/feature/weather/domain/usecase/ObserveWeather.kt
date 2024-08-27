package com.aidannemeth.weatherly.feature.weather.domain.usecase

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWeather @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(): Flow<Weather?> = weatherRepository.observeWeather()
}
