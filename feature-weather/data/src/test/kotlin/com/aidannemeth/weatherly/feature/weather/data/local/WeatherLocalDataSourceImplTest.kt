package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WeatherLocalDataSourceImplTest {
    private val weatherEntity = WeatherEntity(0, Temperature(0.00))

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
    fun `get weather returns weather from db when existing in db`() = runTest {
        val expected = weatherEntity.toWeather().right()
        every { weatherDao.observe() } returns flowOf(weatherEntity)

        val actual = weatherLocalDataSource.getWeather()

        assertEquals(expected, actual)
        verify { weatherDao.observe() }
    }

    @Test
    fun `get weather returns data error from db when not existing in db`() = runTest {
        val expected = NoCachedData.left()
        every { weatherDao.observe() } returns flowOf(null)

        val actual = weatherLocalDataSource.getWeather()

        assertEquals(expected, actual)
        verify { weatherDao.observe() }
    }

    @Test
    fun `observe weather returns weather from db when existing in db`() = runTest {
        val expected = weatherEntity.toWeather().right()
        every { weatherDao.observe() } returns flowOf(weatherEntity)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns data error from db when not existing in db`() = runTest {
        val expected = NoCachedData.left()
        every { weatherDao.observe() } returns flowOf(null)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = NoCachedData.left()
        val secondExpected = weatherEntity.toWeather().right()
        val expectedFlow = flowOf(null, weatherEntity)
        every { weatherDao.observe() } returns expectedFlow

        weatherLocalDataSource.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }
}
