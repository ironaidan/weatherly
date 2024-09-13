package com.aidannemeth.weatherly.feature.common.domain.mapper

import com.aidannemeth.weatherly.feature.common.domain.model.NetworkError

@Suppress("MagicNumber")
fun NetworkError.Companion.fromHttpCode(httpCode: Int): NetworkError = when (httpCode) {
    400 -> NetworkError.BadRequest
    401 -> NetworkError.Unauthorized
    403 -> NetworkError.Forbidden
    404 -> NetworkError.NotFound
    in 500..599 -> NetworkError.ServerError
    else -> NetworkError.Unknown
}
