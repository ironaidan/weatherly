package com.aidannemeth.weatherly.feature.weather.domain.usecase

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.coroutines.DefaultDispatcher
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveWeather @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(): Flow<Either<DataError, Weather>> =
        weatherRepository.observeWeather().flowOn(dispatcher)
}
