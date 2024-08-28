package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceImplTest {

    private val weatherEntity = WeatherEntity(0, 0.00)

    private val weatherDao = mockk<WeatherDao>()

    private val db = mockk<WeatherDatabase> {
        every { weatherDao() } returns weatherDao
    }

    private lateinit var weatherLocalDataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        weatherLocalDataSource = WeatherLocalDataSourceImpl(db)
    }

    @Test
    fun `observe weather returns weather from db when existing in db`() = runTest {
        val expected = weatherEntity.toWeather()
        every { weatherDao.observe() } returns flowOf(weatherEntity)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns null from db when not existing in db`() = runTest {
        val expected = null
        every { weatherDao.observe() } returns flowOf(expected)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `get weather returns weather from db when existing in db`() = runTest {
        val expected = weatherEntity.toWeather()
        every { weatherDao.observe() } returns flowOf(weatherEntity)

        val actual = weatherLocalDataSource.getWeather()

        assertEquals(expected, actual)
    }

    @Test
    fun `get weather returns null from db when not existing in db`() = runTest {
        val expected = null
        every { weatherDao.observe() } returns flowOf(expected)

        val actual = weatherLocalDataSource.getWeather()

        assertEquals(expected, actual)
    }
}
