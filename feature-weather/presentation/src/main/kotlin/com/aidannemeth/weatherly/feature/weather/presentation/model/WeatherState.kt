package com.aidannemeth.weatherly.feature.weather.presentation.model

sealed interface WeatherState {
    data class Data(
        val weatherUiModel: WeatherUiModel
    ) : WeatherState

    data class Error(val message: String) : WeatherState

    data object Loading : WeatherState
}
