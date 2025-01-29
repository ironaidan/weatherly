package com.aidannemeth.weatherly.feature.weather.presentation.model

import com.aidannemeth.weatherly.feature.common.presentation.model.TextUiModel

sealed interface WeatherMetadataState {
    data class Data(
        val weatherUiModel: WeatherMetadataUiModel
    ) : WeatherMetadataState

    data class Error(val message: TextUiModel) : WeatherMetadataState

    data object Loading : WeatherMetadataState
}
