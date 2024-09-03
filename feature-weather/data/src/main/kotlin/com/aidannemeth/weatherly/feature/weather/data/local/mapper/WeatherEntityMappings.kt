package com.aidannemeth.weatherly.feature.weather.data.local.mapper

import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather

fun WeatherEntity.toWeather() = Weather(temp = temp)
