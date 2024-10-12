package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toEntity
import com.aidannemeth.weatherly.feature.weather.data.local.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherEntitySample
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
import kotlin.test.assertEquals

class WeatherLocalDataSourceImplTest {
    private val weatherDao = mockk<WeatherDao>()

    private val db = mockk<WeatherDatabase> {
        every { weatherDao() } returns weatherDao
    }

    private lateinit var weatherLocalDataSource: WeatherLocalDataSourceImpl

    private val weatherEntity = WeatherEntitySample.build()

    @BeforeTest
    fun setup() {
        weatherLocalDataSource = WeatherLocalDataSourceImpl(db)
    }

    @Test
    fun `observe weather returns weather when existing in db`() = runTest {
        val expected = weatherEntity.toWeather()
        every { weatherDao.observe() } returns flowOf(weatherEntity)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            coVerify { db.weatherDao() }
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather returns null when not existing in db`() = runTest {
        val expected = null
        every { weatherDao.observe() } returns flowOf(null)

        weatherLocalDataSource.observeWeather().test {
            assertEquals(expected, awaitItem())
            coVerify { db.weatherDao() }
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `observe weather emits weather and observes updates`() = runTest {
        val firstExpected = null
        val secondExpected = weatherEntity.toWeather()
        val expectedFlow = flowOf(null, weatherEntity)
        every { weatherDao.observe() } returns expectedFlow

        weatherLocalDataSource.observeWeather().test {
            assertEquals(firstExpected, awaitItem())
            assertEquals(secondExpected, awaitItem())
            coVerify { db.weatherDao() }
            verify { weatherDao.observe() }
            awaitComplete()
        }
    }

    @Test
    fun `insert weather inserts weather into db`() = runTest {
        val weather = WeatherSample.build()
        val weatherEntity = weather.toEntity()
        coEvery { weatherDao.insert(weatherEntity) } returns Unit

        weatherLocalDataSource.insertWeather(weather)

        coVerify { db.weatherDao() }
        coVerify { weatherDao.insert(weatherEntity) }
    }
}
