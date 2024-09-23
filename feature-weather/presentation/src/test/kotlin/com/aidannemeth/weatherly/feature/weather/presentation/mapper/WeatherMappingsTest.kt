package com.aidannemeth.weatherly.feature.weather.presentation.mapper

import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import com.aidannemeth.weatherly.feature.weather.presentation.sample.WeatherUiModelSample
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMappingsTest {

    @Test
    fun `weather to weather ui model`() {
        val expected = WeatherUiModelSample.build()

        val actual = WeatherSample.build().toWeatherUiModel()

        assertEquals(expected, actual)
    }
}