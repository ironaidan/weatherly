package com.aidannemeth.weatherly.feature.weather.presentation.model

sealed interface WeatherOperation

internal sealed interface WeatherAction : WeatherOperation

internal sealed interface WeatherEvent : WeatherOperation {
    data class WeatherData(
        val weatherUiModel: WeatherMetadataUiModel,
    ) : WeatherEvent

    data object ErrorLoadingWeather : WeatherEvent
}