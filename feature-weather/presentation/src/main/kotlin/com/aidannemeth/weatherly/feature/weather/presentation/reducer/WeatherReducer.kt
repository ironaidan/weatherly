package com.aidannemeth.weatherly.feature.weather.presentation.reducer

import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherEvent
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherOperation
import com.aidannemeth.weatherly.feature.weather.presentation.model.WeatherState
import javax.inject.Inject

class WeatherReducer @Inject constructor() {
    fun dispatch(
        event: WeatherOperation,
        currentState: WeatherState,
    ): WeatherState = when (event) {
        is WeatherEvent.WeatherData -> WeatherState.Data(
            weatherUiModel = event.weatherUiModel
        )

        WeatherEvent.ErrorLoadingWeather -> currentState.toNewStateForErrorLoading()
    }

    private fun WeatherState.toNewStateForErrorLoading() = when (this) {
        is WeatherState.Data -> this
        is WeatherState.Loading,
        is WeatherState.Error -> WeatherState.Error(
            message = "Error loading weather"
        )
    }
}
