package com.aidannemeth.weatherly.feature.weather.data.local

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherEntitySample
import com.aidannemeth.weatherly.feature.weather.domain.sample.WeatherSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WeatherLocalDataSourceImplTest {
    private val weatherDao = mockk<WeatherDao>()

    private val db = mockk<WeatherDatabase> {
        every { weatherDao() } returns weatherDao
    }

    private val dispatcher = StandardTestDispatcher()

    private val weatherLocalDataSource: WeatherLocalDataSourceImpl by lazy {
        WeatherLocalDataSourceImpl(dispatcher, db)
    }

    private val weather = WeatherSample.build()

    private val weatherEntity = WeatherEntitySample.build()

    @Test
    fun `given cache, when observe weather called, then return weather`() =
        runTest(dispatcher) {
            val expected = weather
            every { weatherDao.observe() } returns flowOf(weatherEntity)

            weatherLocalDataSource.observeWeather().test {
                assertEquals(expected, awaitItem())
                coVerify { db.weatherDao() }
                verify { weatherDao.observe() }
                awaitComplete()
            }
        }

    @Test
    fun `given no cache, when observe weather called, then return null`() =
        runTest(dispatcher) {
            every { weatherDao.observe() } returns flowOf(null)

            weatherLocalDataSource.observeWeather().test {
                assertNull(awaitItem())
                coVerify { db.weatherDao() }
                verify { weatherDao.observe() }
                awaitComplete()
            }
        }

    @Test
    fun `when observe weather called, then return weather and observe updates`() =
        runTest(dispatcher) {
            val expected = weather
            every { weatherDao.observe() } returns flowOf(null, weatherEntity)

            weatherLocalDataSource.observeWeather().test {
                assertNull(awaitItem())
                assertEquals(expected, awaitItem())
                coVerify { db.weatherDao() }
                verify { weatherDao.observe() }
                awaitComplete()
            }
        }

    @Test
    fun `when insert weather called, then entity is inserted`() =
        runTest(dispatcher) {
            coEvery { weatherDao.insert(weatherEntity) } returns Unit

            weatherLocalDataSource.insertWeather(weather)

            coVerify { db.weatherDao() }
            coVerify { weatherDao.insert(weatherEntity) }
        }
}
