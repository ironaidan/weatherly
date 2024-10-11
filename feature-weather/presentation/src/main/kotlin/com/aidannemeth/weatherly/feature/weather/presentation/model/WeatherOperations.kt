package com.aidannemeth.weatherly.feature.weather.presentation.model

sealed interface WeatherOperation

internal sealed interface WeatherAction : WeatherOperation {
    data object  RefreshWeather : WeatherAction
}

internal sealed interface WeatherEvent : WeatherOperation {
    data class WeatherData(
        val payload: WeatherMetadataUiModel,
    ) : WeatherEvent

    data object ErrorLoadingWeather : WeatherEvent
}
