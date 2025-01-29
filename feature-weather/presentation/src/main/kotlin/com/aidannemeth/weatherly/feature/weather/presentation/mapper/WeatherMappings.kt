package com.aidannemeth.weatherly.feature.weather.presentation.mapper

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel
import kotlin.math.roundToInt

internal fun Either<DataError, Weather>.toEvent(): WeatherEvent =
    fold(
        ifLeft = { WeatherEvent.ErrorLoadingWeather },
        ifRight = { WeatherEvent.WeatherData(it.toWeatherUiModel()) },
    )

private fun Weather.toWeatherUiModel(): WeatherMetadataUiModel =
    WeatherMetadataUiModel(
        temperature = TextUiModel("${temperature.value.roundToInt()}℉"),
    )
