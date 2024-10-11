package com.aidannemeth.weatherly.feature.weather.presentation.sample

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel

object WeatherUiModelSample {

    fun build(temperature: String = "293℉") = WeatherMetadataUiModel(
        temperature = temperature,
    )
}
