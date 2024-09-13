package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

object WeatherSample {
    fun build() = Weather(
        latitude = Latitude(33.44),
        longitude = Longitude(-94.04),
        timezone = "America/Chicago",
        timezoneOffset = -18000,
        temperature = Temperature(292.55f),
    )
}
