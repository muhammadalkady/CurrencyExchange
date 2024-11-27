package com.kady.muhammad.core.domain.error

sealed interface DataError : Error {
    /**
     * Enum class representing various network errors.
     */
    enum class Network : DataError {
        /** Request timed out. */
        REQUEST_TIMEOUT,

        /** Too many requests were made in a short period. */
        TOO_MANY_REQUESTS,

        /** No internet connection available. */
        NO_INTERNET,

        /** Server encountered an error. */
        SERVER_ERROR,

        /** Error occurred during serialization. */
        SERIALIZATION,

        /** An unknown error occurred. */
        UNKNOWN,
    }
}