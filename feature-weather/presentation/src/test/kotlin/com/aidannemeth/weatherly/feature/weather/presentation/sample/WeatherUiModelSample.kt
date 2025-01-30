package com.aidannemeth.weatherly.feature.weather.presentation.sample

import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataUiModel

object WeatherUiModelSample {
    fun build() = WeatherMetadataUiModel(
        temperature = TextUiModel("293℉"),
    )

    fun buildIncreasedTemperature(temperature: String = "294℉") =
        WeatherMetadataUiModel(
            temperature = TextUiModel(temperature),
        )
}
