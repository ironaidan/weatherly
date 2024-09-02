package com.aidannemeth.weatherly.feature.weather.data.converters

import androidx.room.TypeConverter
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

class WeatherConverters {
    @TypeConverter
    fun fromFloatToTemperature(value: Float): Temperature =
        Temperature(value)

    @TypeConverter
    fun fromTemperatureToFloat(temperature: Temperature): Float =
        temperature.value
}
