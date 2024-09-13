package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

object WeatherEntitySample {
    fun build() = WeatherEntity(
        latitude = 33.44,
        longitude = -94.04,
        timezone = "America/Chicago",
        timezoneOffset = -18000,
        temperature = Temperature(0.0f),
    )
}
