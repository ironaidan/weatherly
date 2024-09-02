package com.aidannemeth.weatherly.feature.common.domain.model

sealed interface NetworkError {

    /**
     * Request is forbidden
     * 403 error
     */
    data object Forbidden : NetworkError

    /**
     * Network connectivity is not available
     */
    data object NoNetwork : NetworkError

    /**
     * Requested url cannot be found.
     * 404 error
     */
    data object NotFound : NetworkError

    /**
     * Server has encountered an error.
     * 5xx error
     */
    data object ServerError : NetworkError

    /**
     * Request is not authorized
     * 401 error
     */
    data object Unauthorized : NetworkError

    /**
     * Requested host is not reachable
     */
    data object Unreachable : NetworkError

    /**
     * Failed to parse the given response
     */
    data object Parse : NetworkError

    /**
     * Request is not in the correct format
     */
    data object BadRequest : NetworkError

    /**
     * Request is correct and understood but cannot be processed
     */
    data object UnprocessableEntity : NetworkError

    /**
     * This object is not meant to be actively used.
     * Its purpose is to notify the logging tool that a case that should be handled
     * is not and to allow dedicated handling to be put in place.
     */
    data object Unknown : NetworkError

    companion object
}