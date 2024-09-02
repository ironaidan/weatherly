package com.aidannemeth.weatherly.feature.weather.data.remote

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import com.aidannemeth.weatherly.feature.common.domain.mapper.fromHttpCode
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRemoteDataSource {
    override suspend fun getWeather(): Either<DataError.Remote, Weather> =
        weatherApi.getWeather(
            latitude = 34.149956,
            longitude = -118.448891,
            apiKey = "123456789",
            units = "imperial"
        )
            .map {
                Weather(
                    temp = Temperature(it.current.temperature)
                )
            }
            .mapLeft { callError ->
                when (callError) {
                    is HttpError -> DataError.Remote.Http(
                        networkError = NetworkError.fromHttpCode(callError.code),
                        apiErrorInfo = callError.message,
                        isRetryable = false,
                    )

                    is IOError -> DataError.Remote.Http(
                        networkError = NetworkError.NoNetwork,
                        apiErrorInfo = callError.cause.message,
                        isRetryable = true,
                    )

                    is UnexpectedCallError -> DataError.Remote.Http(
                        networkError = NetworkError.Unknown,
                        apiErrorInfo = callError.cause.message,
                        isRetryable = false,
                    )
                }
            }
}
