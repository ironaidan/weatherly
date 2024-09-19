package com.aidannemeth.weatherly.feature.weather.data.repository

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class WeatherRepositoryImplTest {
    private val weather = WeatherSample.build()

    private val weatherLocalDataSource = mockk<WeatherLocalDataSource> {
        every { observeWeather() } returns flowOf(weather)
    }

    private val weatherRemoteDataSource = mockk<WeatherRemoteDataSource> {
        coEvery { getWeather() } returns weather
    }

    private lateinit var weatherRepository: WeatherRepositoryImpl

    @BeforeTest
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(
            weatherLocalDataSource,
            weatherRemoteDataSource,
        )
    }

    @Test
    fun `observe weather interacts with all data sources`() = runTest {
        weatherRepository.observeWeather().test {
            verify { weatherLocalDataSource.observeWeather() }
            coVerify { weatherLocalDataSource.insertWeather(any()) }
            coVerify { weatherRemoteDataSource.getWeather() }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
