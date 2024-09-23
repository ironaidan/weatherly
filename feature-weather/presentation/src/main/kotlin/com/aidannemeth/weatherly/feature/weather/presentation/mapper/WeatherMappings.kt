package com.aidannemeth.weatherly.feature.weather.presentation.mapper

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherUiModel

fun Weather.toWeatherUiModel(): WeatherUiModel =
    WeatherUiModel(
        temperature = temperature.value.toInt().toString(),
    )