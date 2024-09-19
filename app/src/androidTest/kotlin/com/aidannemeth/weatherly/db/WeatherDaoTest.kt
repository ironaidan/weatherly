package com.aidannemeth.weatherly.db

import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherEntitySample
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
class WeatherDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase

    private lateinit var weatherDao: WeatherDao

    private val weatherEntity = WeatherEntitySample.build()

    @BeforeTest
    fun setup() {
        hiltRule.inject()
        weatherDao = db.weatherDao()
    }

    @AfterTest
    fun teardown() {
        db.close()
    }

    @Test
    fun `observe returns weather when existing in db`() = runTest {
        val expected = weatherEntity

        weatherDao.insert(expected)
        val actual = weatherDao.observe().first()

        assertEquals(actual, expected)
    }

    @Test
    fun `observe returns null when weather not existing in db`() = runTest {
        val expected = null

        val actual = weatherDao.observe().first()

        assertEquals(actual, expected)
    }

    @Test
    fun `db only ever contains one row`() = runTest {
        val expected = 1

        weatherDao.insert(weatherEntity)
        weatherDao.insert(weatherEntity.copy(temperature = Temperature(1.0f)))
        weatherDao.insert(weatherEntity.copy(temperature = Temperature(2.0f)))
        val actual = weatherDao.count()

        assertEquals(actual, expected)
    }
}
