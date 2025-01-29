package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.data.remote.resource.CurrentWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.DailyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.FeelsLikeResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.HourlyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.MinutelyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.TemperatureResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.WeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

@Suppress("LongMethod")
object WeatherResponseSample {
    fun build() =
        WeatherResponse(
            latitude = Latitude(value = 33.44),
            longitude = Longitude(value = -94.04),
            timezone = "America/Chicago",
            timezoneOffset = -18000,
            current = CurrentWeatherResource(
                dt = 1684929490,
                sunrise = 1684926645,
                sunset = 1684977332,
                temperature = Temperature(value = 292.55f),
                feelsLike = Temperature(value = 292.87f),
                pressure = 1014,
                humidity = 89,
                dewPoint = 290.69f,
                uvi = 0.16f,
                clouds = 53,
                visibility = 10000,
                windSpeed = 3.13f,
                windDirection = 93,
                weather = listOf(
                    WeatherResource(
                        id = 803,
                        main = "Clouds",
                        description = "broken clouds",
                        icon = "04d"
                    )
                )
            ),
            minutely = listOf(
                MinutelyWeatherResource(
                    dt = 1684929540,
                    precipitation = 0
                )
            ),
            hourly = listOf(
                HourlyWeatherResource(
                    dt = 1684926000,
                    temperature = Temperature(value = 292.01f),
                    feelsLike = Temperature(value = 292.33f),
                    pressure = 1014,
                    humidity = 91,
                    dewPoint = 290.51f,
                    uvi = 0.0f,
                    clouds = 54,
                    visibility = 10000,
                    windSpeed = 2.58f,
                    windDirection = 86,
                    windGust = 5.88f,
                    weather = listOf(
                        WeatherResource(
                            id = 803,
                            main = "Clouds",
                            description = "broken clouds",
                            icon = "04n"
                        )
                    ),
                    pop = 0.15f
                )
            ),
            daily = listOf(
                DailyWeatherResource(
                    dt = 1684951200,
                    sunrise = 1684926645,
                    sunset = 1684977332,
                    moonrise = 1684941060,
                    moonset = 1684905480,
                    moonPhase = 0.16f,
                    summary = "Expect a day of partly cloudy with rain",
                    temperature = TemperatureResource(
                        day = Temperature(value = 299.03f),
                        min = Temperature(value = 290.69f),
                        max = Temperature(value = 300.35f),
                        night = Temperature(value = 291.45f),
                        eve = Temperature(value = 297.51f),
                        morn = Temperature(value = 292.55f),
                    ),
                    feelsLike = FeelsLikeResource(
                        day = Temperature(value = 299.21f),
                        night = Temperature(value = 291.37f),
                        eve = Temperature(value = 297.86f),
                        morn = Temperature(value = 292.87f),
                    ),
                    pressure = 1016,
                    humidity = 59,
                    dewPoint = 290.48f,
                    windSpeed = 3.98f,
                    windDirection = 76,
                    windGust = 8.92f,
                    weather = listOf(
                        WeatherResource(
                            id = 500,
                            main = "Rain",
                            description = "light rain",
                            icon = "10d"
                        )
                    ),
                    clouds = 92,
                    pop = 0.47f,
                    uvi = 9.23f,
                )
            ),
        )
}
