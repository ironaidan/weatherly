package com.aidannemeth.weatherly.feature.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.MinutelyWeatherResource
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

@Entity
data class WeatherEntity(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val temperature: Temperature,
//    val minutely: List<MinutelyWeatherEntity>,
    @PrimaryKey val id: Long? = 1,
)

@Entity
data class MinutelyWeatherEntity(
    val dt: Long,
    val precipitation: Int,
)
