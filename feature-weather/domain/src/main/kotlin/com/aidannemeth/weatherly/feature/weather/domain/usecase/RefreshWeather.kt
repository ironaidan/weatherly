package com.aidannemeth.weatherly.feature.weather.domain.usecase

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.coroutines.DefaultDispatcher
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshWeather @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(): Either<DataError.Remote, Weather> =
        withContext(dispatcher) {
            weatherRepository.refreshWeather()
        }
}
