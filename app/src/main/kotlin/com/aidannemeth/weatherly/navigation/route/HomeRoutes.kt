package com.aidannemeth.weatherly.navigation.route

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aidannemeth.weatherly.feature.weather.presentation.ui.WeatherScreenContainer
import com.aidannemeth.weatherly.navigation.model.Destination.Screen.Weather

internal fun NavGraphBuilder.addWeatherScreen() {
    composable<Weather> {
        WeatherScreenContainer()
    }
}
