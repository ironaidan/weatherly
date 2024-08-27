package com.aidannemeth.weatherly

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AppDatabaseTest {
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
    fun writeAndReadWeather() = runTest {
        val expected = WeatherEntity(
            id = 0,
            temp = 0.0,
        )

        weatherDao.insert(expected)
        val actual = weatherDao.observe().first()

        assertThat(actual, equalTo(expected))
    }
}
