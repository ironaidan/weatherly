package com.aidannemeth.weatherly.feature.weather.presentation.model

data class WeatherState(
    val isRefreshing: Boolean,
    val weatherMetadataState: WeatherMetadataState,
) {
    companion object {

        val Loading = WeatherState(
            isRefreshing = false,
            weatherMetadataState = WeatherMetadataState.Loading,
        )
    }
}
