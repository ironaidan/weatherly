package com.aidannemeth.weatherly.feature.weather.data.sample

import com.aidannemeth.weatherly.feature.weather.data.remote.resource.AlertResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.CurrentWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.DailyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.FeelsLikeResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.HourlyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.MinutelyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.TemperatureResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.WeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse

object WeatherResponseSample {
    fun build() =
        WeatherResponse(
//            latitude = 33.44,
//            longitude = -94.04,
//            timezone = "America/Chicago",
//            timezoneOffset = -18000,
            current = CurrentWeatherResource(
//                dt = 1684929490,
//                sunrise = 1684926645,
//                sunset = 1684977332,
                temperature = 292.55f,
//                feelsLike = 292.87f,
//                pressure = 1014,
//                humidity = 89,
//                dewPoint = 290.69f,
//                uvi = 0.16f,
//                clouds = 53,
//                visibility = 10000,
//                windSpeed = 3.13f,
//                windDirection = 93,
//                windGust = 6.71f,
//                weather = listOf(
//                    WeatherResource(
//                        id = 803,
//                        main = "Clouds",
//                        description = "broken clouds",
//                        icon = "04d"
//                    )
//                )
//            ),
//            minutely = listOf(
//                MinutelyWeatherResource(
//                    dt = 1684929540,
//                    precipitation = 0
//                )
//            ),
//            hourly = listOf(
//                HourlyWeatherResource(
//                    dt = 1684926000,
//                    temperature = 292.01f,
//                    feelsLike = 292.33f,
//                    pressure = 1014,
//                    humidity = 91,
//                    dewPoint = 290.51f,
//                    uvi = 0.0f,
//                    clouds = 54,
//                    visibility = 10000,
//                    windSpeed = 2.58f,
//                    windDirection = 86,
//                    windGust = 5.88f,
//                    weather = listOf(
//                        WeatherResource(
//                            id = 803,
//                            main = "Clouds",
//                            description = "broken clouds",
//                            icon = "04n"
//                        )
//                    ),
//                    pop = 0.15f
//                )
//            ),
//            daily = listOf(
//                DailyWeatherResource(
//                    dt = 1684951200,
//                    sunrise = 1684926645,
//                    sunset = 1684977332,
//                    moonrise = 1684941060,
//                    moonset = 1684905480,
//                    moonPhase = 0.16f,
//                    summary = "Expect a day of partly cloudy with rain",
//                    temperature = TemperatureResource(
//                        day = 299.03f,
//                        min = 290.69f,
//                        max = 300.35f,
//                        night = 291.45f,
//                        eve = 297.51f,
//                        morn = 292.55f,
//                    ),
//                    feelsLike = FeelsLikeResource(
//                        day = 299.21f,
//                        night = 291.37f,
//                        eve = 297.86f,
//                        morn = 292.87f,
//                    ),
//                    pressure = 1016,
//                    humidity = 59,
//                    dewPoint = 290.48f,
//                    windSpeed = 3.98f,
//                    windDirection = 76,
//                    windGust = 8.92f,
//                    weather = listOf(
//                        WeatherResource(
//                            id = 500,
//                            main = "Rain",
//                            description = "light rain",
//                            icon = "10d"
//                        )
//                    ),
//                    clouds = 92,
//                    pop = 0.47f,
////                    rain = 0.15f,
//                    uvi = 9.23f,
//                )
//            ),
////            alerts = listOf(
////                AlertResource(
////                    senderName = "NWS Philadelphia - Mount Holly (New Jersey, Delaware, Southeastern Pennsylvania)",
////                    event = "Small Craft Advisory",
////                    start = 1684952747,
////                    end = 1684988747,
////                    description = "...SMALL CRAFT ADVISORY REMAINS IN EFFECT FROM 5 PM THIS\n" +
////                            "AFTERNOON TO 3 AM EST FRIDAY...\n" +
////                            "* WHAT...North winds 15 to 20 kt with gusts up to 25 kt and seas\n" +
////                            "3 to 5 ft expected.\n" +
////                            "* WHERE...Coastal waters from Little Egg Inlet to Great Egg\n" +
////                            "Inlet NJ out 20 nm, Coastal waters from Great Egg Inlet to\n" +
////                            "Cape May NJ out 20 nm and Coastal waters from Manasquan Inlet\n" +
////                            "to Little Egg Inlet NJ out 20 nm.\n" +
////                            "* WHEN...From 5 PM this afternoon to 3 AM EST Friday.\n" +
////                            "* IMPACTS...Conditions will be hazardous to small craft.",
////                    tags = emptyList()
////                ),
            )
        )
}
