package com.aidannemeth.weatherly.feature.weather.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveWeatherTest {
    private val weatherRepository = mockk<WeatherRepository>()

    private lateinit var observeWeather: ObserveWeather

    private val weather = WeatherSample.build()

    private lateinit var expected: Either<DataError.Local, Weather>

    @BeforeTest
    fun setup() {
        observeWeather = ObserveWeather(weatherRepository)
    }

    @Test
    fun `observe weather returns weather when existing in repository`() = runTest {
        expected = weather.right()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns data error when not existing in repository`() = runTest {
        expected = NoCachedData.left()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = NoCachedData.left()
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
