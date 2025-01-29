package com.aidannemeth.weatherly.feature.weather.presentation.model

data class WeatherState(
    val weatherMetadataState: WeatherMetadataState,
) {
    companion object {
        val Loading = WeatherState(
            weatherMetadataState = WeatherMetadataState.Loading,
        )
    }
}
