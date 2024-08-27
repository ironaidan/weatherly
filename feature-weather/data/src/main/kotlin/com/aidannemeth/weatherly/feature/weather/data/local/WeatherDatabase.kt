package com.aidannemeth.weatherly.feature.weather.data.local

import com.aidannemeth.weatherly.feature.weather.data.local.dao.WeatherDao

interface WeatherDatabase {
    fun weatherDao(): WeatherDao
}
