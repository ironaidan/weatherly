package com.aidannemeth.weatherly.feature.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

@Entity
data class WeatherEntity(
    val latitude: Latitude,
    val longitude: Longitude,
    val timezone: String,
    val timezoneOffset: Int,
    val temperature: Temperature,
    @PrimaryKey val id: Long? = 1,
)
