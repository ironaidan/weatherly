package com.aidannemeth.weatherly.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aidannemeth.weatherly.feature.weather.data.converters.WeatherConverters
import com.aidannemeth.weatherly.feature.weather.data.local.WeatherDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
@TypeConverters(WeatherConverters::class)
abstract class AppDatabase : RoomDatabase(), WeatherDatabase
