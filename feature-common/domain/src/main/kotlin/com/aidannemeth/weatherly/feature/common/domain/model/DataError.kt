package com.aidannemeth.weatherly.feature.common.domain.model

sealed interface DataError {
    sealed interface Local : DataError {
        data object NoCachedData : Local
    }
}
