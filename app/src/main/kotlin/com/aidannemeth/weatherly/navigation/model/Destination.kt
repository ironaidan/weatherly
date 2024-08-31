package com.aidannemeth.weatherly.navigation.model

import kotlinx.serialization.Serializable

sealed class Destination {
    object Screen {
        @Serializable
        data object Weather : Destination()
    }
}
