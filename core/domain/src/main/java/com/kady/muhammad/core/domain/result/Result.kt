package com.kady.muhammad.core.domain.result

import com.kady.muhammad.core.domain.error.Error


/**
 * Type alias for domain-specific errors.
 */
typealias DomainError = Error

/**
 * A sealed interface representing a result that can either be a success or an error.
 *
 * @param T The type of the data in case of success.
 * @param E The type of the error in case of failure, which must extend [DomainError].
 */
sealed interface Result<out T, out E : DomainError> {

    /**
     * Represents a successful result containing data.
     *
     * @param T The type of the data.
     * @param E The type of the error, which must extend [DomainError].
     * @property data The data of the successful result.
     */
    data class Success<out T, out E : DomainError>(val data: T) : Result<T, E>

    /**
     * Represents an error result containing an error.
     *
     * @param T The type of the data.
     * @param E The type of the error, which must extend [DomainError].
     * @property error The error of the result.
     */
    data class Error<out T, out E : DomainError>(val error: E) : Result<T, E>
}


inline fun <T, E : Error, R> Result<T, E>.map(mapper: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error   -> Result.Error(error)
        is Result.Success -> Result.Success(mapper(data))
    }
}

/**
 * Executes the given action if the result is a success.
 *
 * @param action The action to be executed with the successful result's data.
 * @return The original result.
 */
inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error   -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

/**
 * Executes the given action if the result is an error.
 *
 * @param action The action to be executed with the error.
 * @return The original result.
 */
inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error   -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}