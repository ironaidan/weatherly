package com.aidannemeth.weatherly.feature.weather.domain.usecase

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ObserveWeatherTest {
    private val weather = Weather(0.00)

    private val weatherRepository = mockk<WeatherRepository>()

    private lateinit var observeWeather: ObserveWeather

    @Before
    fun setUp() {
        observeWeather = ObserveWeather(weatherRepository)
    }

    @Test
    fun `observe weather returns weather when existing in repository`() = runTest {
        val expected = weather
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns null when not existing in repository`() = runTest {
        val expected = null
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = null
        val secondExpected = Weather(0.00)
        val expectedFlow = flowOf(firstExpected, secondExpected)
        every { weatherRepository.observeWeather() } returns expectedFlow

        observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }
}
