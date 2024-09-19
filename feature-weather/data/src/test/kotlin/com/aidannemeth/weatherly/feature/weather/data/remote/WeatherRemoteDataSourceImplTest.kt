package com.aidannemeth.weatherly.feature.weather.data.remote

import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherRemoteDataSourceImplTest {
    private val weatherApi = mockk<WeatherApi>()

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private val weatherResponse = WeatherResponseSample.build()

    @BeforeTest
    fun setup() {
        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherApi)
    }

    @Test
    fun `get weather returns weather from network when successful`() = runTest {
        val expected = weatherResponse.toWeather()
        coEvery { weatherApi.getWeather(any(), any(), any(), any()) } returns weatherResponse

        val actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
        coVerify { weatherApi.getWeather(any(), any(), any(), any()) }
    }

    @Test
    fun `get weather throws exception when api throws exception`() = runTest {
        coEvery { weatherApi.getWeather(any(), any(), any(), any()) } throws IOException()

        assertThrows(IOException::class.java) {
            runBlocking {
                weatherRemoteDataSource.getWeather()
            }
        }
        coVerify { weatherApi.getWeather(any(), any(), any(), any()) }
    }
}
