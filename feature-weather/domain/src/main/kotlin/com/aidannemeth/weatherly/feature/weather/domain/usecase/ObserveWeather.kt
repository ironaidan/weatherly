package com.aidannemeth.weatherly.feature.weather.domain.usecase

import arrow.core.Either
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveWeather @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(): Flow<Either<NoCachedData, Weather>> =
        weatherRepository.observeWeather()
            .flowOn(Dispatchers.Default)
}
