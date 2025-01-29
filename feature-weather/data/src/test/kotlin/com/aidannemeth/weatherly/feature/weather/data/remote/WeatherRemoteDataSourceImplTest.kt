package com.aidannemeth.weatherly.feature.weather.data.remote

import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WeatherRemoteDataSourceImplTest {
    private val dispatcher = StandardTestDispatcher()

    private val weatherApi = mockk<WeatherApi>()

    private val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(dispatcher, weatherApi)
    }

    private val weather = WeatherSample.build()

    private val weatherResponse = WeatherResponseSample.build()

    @Test
    fun `given 200, when get weather called, then return weather`() =
        runTest(dispatcher) {
            val expected = weather
            coEvery { weatherApi.getWeather(any(), any(), any(), any()) } returns weatherResponse

            val actual = weatherRemoteDataSource.getWeather()

            assertEquals(expected, actual)
            coVerify { weatherApi.getWeather(any(), any(), any(), any()) }
        }

    @Test
    fun `given exception, when get weather called, then throw exception`() =
        runTest(dispatcher) {
            coEvery { weatherApi.getWeather(any(), any(), any(), any()) } throws IOException()

            assertFailsWith<IOException> {
                weatherRemoteDataSource.getWeather()
            }
            coVerify { weatherApi.getWeather(any(), any(), any(), any()) }
        }
}
