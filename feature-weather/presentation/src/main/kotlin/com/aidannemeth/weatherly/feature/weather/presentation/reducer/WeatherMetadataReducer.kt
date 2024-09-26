package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import javax.inject.Inject

class WeatherMetadataReducer @Inject constructor() {
    internal fun dispatch(
        operation: WeatherOperation,
        currentState: WeatherMetadataState,
    ): WeatherMetadataState = when (operation) {
        is WeatherEvent.WeatherData -> WeatherMetadataState.Data(
            weatherUiModel = operation.payload
        )

        WeatherEvent.ErrorLoadingWeather -> currentState.toNewStateForErrorLoading()
        WeatherAction.RefreshWeather -> TODO()
    }

    private fun WeatherMetadataState.toNewStateForErrorLoading() = when (this) {
        is WeatherMetadataState.Data -> this
        is WeatherMetadataState.Loading,
        is WeatherMetadataState.Error -> WeatherMetadataState.Error(
            message = "Error loading weather",
        )
    }
}
