package com.aidannemeth.weatherly.feature.weather.presentation.mapper

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel
import kotlin.math.roundToInt

fun Weather.toWeatherUiModel(): WeatherMetadataUiModel =
    WeatherMetadataUiModel(
        temperature = "${temperature.value.roundToInt()}℉",
    )