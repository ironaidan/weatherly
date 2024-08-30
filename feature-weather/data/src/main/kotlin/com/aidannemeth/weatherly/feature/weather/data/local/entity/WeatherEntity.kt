package com.aidannemeth.weatherly.feature.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

@Entity
data class WeatherEntity(
    @PrimaryKey val id: Long,
    val temp: Temperature,
)
