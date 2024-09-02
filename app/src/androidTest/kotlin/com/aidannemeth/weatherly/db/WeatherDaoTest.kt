package com.aidannemeth.weatherly.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WeatherDaoTest {
    private lateinit var weatherDao: WeatherDao
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        weatherDao = db.weatherDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun `observe returns weather when existing in db`() = runTest {
        val expected = WeatherEntity(0, Temperature(0.0f))

        weatherDao.insert(expected)
        val actual = weatherDao.observe().first()

        assertThat(actual, equalTo(expected))
    }

    @Test
    @Throws(Exception::class)
    fun `observe returns null when weather not existing in db`() = runTest {
        val expected = null

        val actual = weatherDao.observe().first()

        assertThat(actual, equalTo(expected))
    }
}
