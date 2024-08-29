package com.aidannemeth.weatherly.feature.weather.data.repository

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WeatherRepositoryImplTest {
    private val weather = Weather(0.00)

    private val weatherLocalDataSource = mockk<WeatherLocalDataSource>()

    private lateinit var weatherRepository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        weatherRepository = WeatherRepositoryImpl(weatherLocalDataSource)
    }

    @Test
    fun `get weather returns weather when existing locally`() = runTest {
        val expected = weather
        coEvery { weatherLocalDataSource.getWeather() } returns expected

        val actual = weatherRepository.getLocalWeather()
        assertEquals(expected, actual)
        coVerify { weatherLocalDataSource.getWeather() }
    }

    @Test
    fun `get weather returns null when not existing locally`() = runTest {
        val expected = null
        coEvery { weatherLocalDataSource.getWeather() } returns expected

        val actual = weatherRepository.getLocalWeather()
        assertEquals(expected, actual)
        coVerify { weatherLocalDataSource.getWeather() }
    }

    @Test
    fun `observe weather returns weather when existing locally`() = runTest {
        val expected = weather
        every { weatherLocalDataSource.observeWeather() } returns flowOf(expected)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherLocalDataSource.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns null when not existing locally`() = runTest {
        val expected = null
        every { weatherLocalDataSource.observeWeather() } returns flowOf(expected)

        weatherRepository.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherLocalDataSource.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = null
        val secondExpected = weather
        val expectedFlow = flowOf(firstExpected, secondExpected)
        every { weatherLocalDataSource.observeWeather() } returns expectedFlow

        weatherRepository.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            verify { weatherLocalDataSource.observeWeather() }
            awaitComplete()
        }
    }
}
