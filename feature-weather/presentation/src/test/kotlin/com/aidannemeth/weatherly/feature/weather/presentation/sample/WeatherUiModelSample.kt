package com.aidannemeth.weatherly.feature.weather.presentation.sample

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherUiModel

object WeatherUiModelSample {

    fun build(temperature: String = "293℉") = WeatherUiModel(
        temperature = temperature,
    )
}
