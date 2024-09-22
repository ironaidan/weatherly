package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import javax.inject.Inject

class WeatherMetadataReducer @Inject constructor() {
    fun dispatch(
        event: WeatherOperation,
        currentState: WeatherMetadataState,
    ): WeatherMetadataState = when (event) {
        is WeatherEvent.WeatherData -> WeatherMetadataState.Data(
            weatherUiModel = event.weatherUiModel
        )

        WeatherEvent.ErrorLoadingWeather -> currentState.toNewStateForErrorLoading()
    }

    private fun WeatherMetadataState.toNewStateForErrorLoading() = when (this) {
        is WeatherMetadataState.Data -> this
        is WeatherMetadataState.Loading,
        is WeatherMetadataState.Error -> WeatherMetadataState.Error(
            message = "Error loading weather",
        )
    }
}
