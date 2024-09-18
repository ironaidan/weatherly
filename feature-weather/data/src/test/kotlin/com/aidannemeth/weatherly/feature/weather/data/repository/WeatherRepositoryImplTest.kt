package com.aidannemeth.weatherly.feature.weather.data.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WeatherRepositoryImplTest {
    private val weatherLocalDataSource = mockk<WeatherLocalDataSource>()

    private val weatherRemoteDataSource = mockk<WeatherRemoteDataSource>()

    private lateinit var weatherRepository: WeatherRepositoryImpl

    private val weather = WeatherSample.build()

    @Before
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(
            weatherLocalDataSource,
            weatherRemoteDataSource,
        )
    }

    @Test
    fun `observe weather initially returns loading when cache is empty`() = runTest {
        coEvery { weatherLocalDataSource.observeWeather() } returns flowOf(null)

        weatherRepository.observeWeather().test {
            assertEquals(DataError.Remote.Loading.left(), awaitItem())
        }
    }

    @Test
    fun `observe weather returns weather when cached`() = runTest {
        coEvery { weatherLocalDataSource.observeWeather() } returns flowOf(weather)
        coEvery { weatherRemoteDataSource.getWeather() } returns weather

        weatherRepository.observeWeather().test {
            assertEquals(weather.right(), awaitItem())
            assertEquals(DataError.Remote.Loading.left(), awaitItem())
            assertEquals(weather.right(), awaitItem())
        }
    }
}
