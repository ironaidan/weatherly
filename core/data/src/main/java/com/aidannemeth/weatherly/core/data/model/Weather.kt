package com.aidannemeth.weatherly.core.data.model

import com.aidannemeth.weatherly.core.model.Weather
import com.aidannemeth.weatherly.core.network.model.NetworkWeather

fun NetworkWeather.asExternalModel() = Weather(
    weather = weather,
    description = description,
    icon = icon,
    temperature = temperature,
    feelsLike = feelsLike,
    temperatureMin = temperatureMin,
    temperatureMax = temperatureMax,
)
