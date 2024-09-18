package com.aidannemeth.weatherly.feature.weather.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ObserveWeatherTest {
    private val dispatcher = StandardTestDispatcher()

    private val weatherRepository = mockk<WeatherRepository>()

    private lateinit var observeWeather: ObserveWeather

    private val weather = WeatherSample.build()

    private lateinit var expected: Either<DataError.Local, Weather>

    @Before
    fun setup() {
        observeWeather = ObserveWeather(dispatcher, weatherRepository)
    }

    @Test
    fun `observe weather returns weather when existing in repository`() = runTest(dispatcher) {
        expected = weather.right()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns data error when not existing in repository`() = runTest(dispatcher) {
        expected = NoCachedData.left()
        every { weatherRepository.observeWeather() } returns flowOf(expected)

        observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherRepository.observeWeather() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest(dispatcher) {
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
