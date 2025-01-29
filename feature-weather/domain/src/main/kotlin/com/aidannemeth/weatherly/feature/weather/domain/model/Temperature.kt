package com.aidannemeth.weatherly.feature.weather.domain.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Temperature(val value: Float)
