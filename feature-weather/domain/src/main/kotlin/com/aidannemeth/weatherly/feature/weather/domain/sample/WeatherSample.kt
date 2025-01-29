package com.aidannemeth.weatherly.feature.weather.domain.sample

import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather

object WeatherSample {
    fun build() = Weather(
        latitude = Latitude(value = 33.44),
        longitude = Longitude(value = -94.04),
        timezone = "America/Chicago",
        timezoneOffset = -18000,
        temperature = Temperature(value = 292.55f),
    )
}
