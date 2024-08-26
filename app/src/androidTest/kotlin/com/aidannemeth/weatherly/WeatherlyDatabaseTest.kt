package com.aidannemeth.weatherly

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.aidannemeth.weatherly.db.WeatherlyDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WeatherlyDatabaseTest {
    private lateinit var weatherDao: WeatherDao
    private lateinit var db: WeatherlyDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherlyDatabase::class.java,
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
        val actual = weatherDao.get().first()

        assertThat(actual, equalTo(expected))
    }
}
