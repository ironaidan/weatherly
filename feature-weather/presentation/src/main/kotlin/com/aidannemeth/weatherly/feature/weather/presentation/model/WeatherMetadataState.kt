package com.aidannemeth.weatherly.feature.weather.presentation.model

sealed interface WeatherMetadataState {
    data class Data(
        val weatherUiModel: WeatherMetadataUiModel
    ) : WeatherMetadataState

    data class Error(val message: String) : WeatherMetadataState

    data object Loading : WeatherMetadataState
}
