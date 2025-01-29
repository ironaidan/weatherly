package com.aidannemeth.weatherly.db

import app.cash.turbine.test
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.sample.WeatherEntitySample
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
    fun when_inserting_then_observe_updates() = runTest {
        val updatedEntity = weatherEntity.copy(
            temperature = Temperature(value = 100.0f)
        )

        weatherDao.observe().test {
            assertNull(awaitItem())
            weatherDao.insert(weatherEntity)
            assertEquals(weatherEntity, awaitItem())
            weatherDao.insert(updatedEntity)
            assertEquals(updatedEntity, awaitItem())
        }
    }

    @Test
    fun given_db_contains_weather_when_deleting_then_count_is_zero() =
        runTest {
            val expected = 0
            weatherDao.insert(weatherEntity)

            weatherDao.deleteAll()

            val actual = weatherDao.count()
            assertEquals(expected, actual)
        }
}
