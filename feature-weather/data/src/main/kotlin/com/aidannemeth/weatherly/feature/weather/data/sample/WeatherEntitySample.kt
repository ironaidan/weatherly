package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

object WeatherEntitySample {
    fun build() = WeatherEntity(
        latitude = Latitude(value = 33.44),
        longitude = Longitude(value = -94.04),
        timezone = "America/Chicago",
        timezoneOffset = -18000,
        temperature = Temperature(value = 292.55f),
    )
}
