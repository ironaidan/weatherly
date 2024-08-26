package com.aidannemeth.weatherly.feature.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey val id: Int,
)
