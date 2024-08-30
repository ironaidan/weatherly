package com.aidannemeth.weatherly.feature.weather.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import com.aidannemeth.weatherly.feature.weather.domain.repository.DataError
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
    private val weather = Weather(Temperature(0.00))

    private val weatherRepository = mockk<WeatherRepository>()

    private lateinit var observeWeather: ObserveWeather

    @Before
    fun setUp() {
        observeWeather = ObserveWeather(weatherRepository)
    }

    @Test
    fun `observe weather returns weather when existing in repository`() = runTest {
        val expected = weather.right()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns data error when not existing in repository`() = runTest {
        val expected = DataError.Local.NoCachedData.left()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = DataError.Local.NoCachedData.left()
        val secondExpected = weather.right()
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
