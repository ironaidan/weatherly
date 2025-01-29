package com.aidannemeth.weatherly.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.WeatherDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), WeatherDatabase
