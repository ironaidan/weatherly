package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherAction
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherMetadataState
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import javax.inject.Inject

class WeatherReducer @Inject constructor() {
    internal fun getState(
        operation: WeatherOperation,
        currentState: WeatherState,
    ): WeatherState = when (operation) {
        is WeatherEvent.WeatherData -> WeatherState(
            weatherMetadataState = WeatherMetadataState.Data(
                weatherUiModel = operation.payload,
            ),
        )

        WeatherEvent.ErrorLoadingWeather -> currentState.copy(
            weatherMetadataState = when (currentState.weatherMetadataState) {
                is WeatherMetadataState.Data -> currentState.weatherMetadataState
                WeatherMetadataState.Loading,
                is WeatherMetadataState.Error -> WeatherMetadataState.Error(
                    message = "Error loading weather",
                )
            }
        )

        WeatherAction.RefreshWeather -> currentState
    }
}
