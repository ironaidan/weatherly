package com.aidannemeth.weatherly.feature.weather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert
    fun insert(weather: WeatherEntity)

    @Query("SELECT * FROM WeatherEntity")
    fun get(): Flow<WeatherEntity?>
}
