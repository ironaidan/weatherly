package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherEntitySample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WeatherLocalDataSourceImplTest {
    private val weatherDao = mockk<WeatherDao>()

    private val db = mockk<WeatherDatabase> {
        every { weatherDao() } returns weatherDao
    }

    private lateinit var weatherLocalDataSource: WeatherLocalDataSourceImpl

    private val weatherEntity = WeatherEntitySample.build()

    @Before
    fun setup() {
        weatherLocalDataSource = WeatherLocalDataSourceImpl(db)
    }

    @Test
    fun `observe weather returns weather when existing in db`() = runTest {
        every { weatherDao.observe() } returns flowOf(weatherEntity)
        val expected = weatherEntity.toWeather()

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns null when not existing in db`() = runTest {
        every { weatherDao.observe() } returns flowOf(null)
        val expected = null

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val expectedFlow = flowOf(null, weatherEntity)
        every { weatherDao.observe() } returns expectedFlow
        val firstExpected = null
        val secondExpected = weatherEntity.toWeather()

        weatherLocalDataSource.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }
}
