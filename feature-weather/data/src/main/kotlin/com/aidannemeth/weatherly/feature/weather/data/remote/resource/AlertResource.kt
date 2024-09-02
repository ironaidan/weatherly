package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertResource(
    @SerialName("sender_name")
    val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>,
)
