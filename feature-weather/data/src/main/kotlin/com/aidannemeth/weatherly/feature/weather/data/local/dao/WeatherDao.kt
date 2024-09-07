package com.aidannemeth.weatherly.feature.weather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity)

    @Query("SELECT * FROM WeatherEntity LIMIT 1")
    fun observe(): Flow<WeatherEntity?>

    @Query("SELECT COUNT(id) FROM WeatherEntity")
    suspend fun count(): Int
}
