package com.aidannemeth.weatherly.feature.weather.data.converters

import androidx.room.TypeConverter
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

class WeatherConverters {
    @TypeConverter
    fun fromDoubleToTemperature(value: Double): Temperature =
        Temperature(value)

    @TypeConverter
    fun fromTemperatureToDouble(temperature: Temperature): Double =
        temperature.value
}
