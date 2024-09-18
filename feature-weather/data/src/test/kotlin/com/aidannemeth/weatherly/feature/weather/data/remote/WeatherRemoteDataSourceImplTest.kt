package com.aidannemeth.weatherly.feature.weather.data.remote

import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherResponseSample
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

class WeatherRemoteDataSourceImplTest {

    private val weatherApi = mockk<WeatherApi>()

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private val weatherResponse = WeatherResponseSample.build()

    @Before
    fun setup() {
        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherApi)
    }

    @Test
    fun `get weather returns weather from network when successful`() = runTest {
        coEvery { weatherApi.getWeather(any(), any(), any(), any()) } returns weatherResponse
        val expected = weatherResponse.toWeather()

        val actual = weatherRemoteDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather throws exception when api throws exception`() = runTest {
        coEvery { weatherApi.getWeather(any(), any(), any(), any()) } throws IOException()

        assertThrows(IOException::class.java) {
            runBlocking {
                weatherRemoteDataSource.getWeather()
            }
        }
    }
}
