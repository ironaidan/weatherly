package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceImplTest {

    private val weatherEntity = WeatherEntity(0, 0.00)

    private val weatherDao = mockk<WeatherDao>()
    {
        coEvery { insert(any()) } just Runs
        every { observe() } returns flowOf(weatherEntity)
    }

    private val db = mockk<WeatherDatabase> {
        every { weatherDao() } returns weatherDao
    }

    private lateinit var weatherLocalDataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        weatherLocalDataSource = WeatherLocalDataSourceImpl(db)
    }

    @Test
    fun `observe weather returns weather from db when existing`() = runTest {
        val expected = weatherEntity.toWeather().copy(temp = 0.0)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
