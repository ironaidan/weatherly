package com.aidannemeth.weatherly.feature.weather.data.repository

import app.cash.turbine.test
import arrow.core.right
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherLocalDataSourceTestFake
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherRemoteDataSourceTestFake
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()

    private val weatherLocalDataSource = WeatherLocalDataSourceTestFake()

    private val weatherRemoteDataSource = WeatherRemoteDataSourceTestFake()

    private val weatherRepository: WeatherRepositoryImpl by lazy {
        WeatherRepositoryImpl(
            dispatcher = dispatcher,
            weatherLocalDataSource = weatherLocalDataSource,
            weatherRemoteDataSource = weatherRemoteDataSource,
        )
    }

    private val cachedWeather = WeatherSample.build()

    private val remoteWeather = WeatherSample.buildIncreasedTemperature()

    @Test
    fun `given cache, when observe weather called, then return cache then remote`() =
        runTest(dispatcher) {
            weatherRepository.observeWeather().test {
                assertEquals(cachedWeather.right(), awaitItem())
                assertEquals(remoteWeather.right(), awaitItem())
            }
        }

    @Test
    fun `given no cache, when observe weather called, then return null then remote`() =
        runTest(dispatcher) {
            weatherLocalDataSource.weatherFlow.value = null
            weatherRepository.observeWeather().test {
                assertEquals(remoteWeather.right(), awaitItem())
            }
        }

    @Test
    fun `given 200, when refresh weather called, then return remote`() =
        runTest(dispatcher) {
        val expected = remoteWeather.right()

        val actual = weatherRepository.refreshWeather()
        assertEquals(expected, actual)
    }
}
