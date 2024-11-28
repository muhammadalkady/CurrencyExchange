package com.kady.muhammad.exchange.network

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import retrofit2.HttpException

inline fun <reified T> mapHttpExceptionToResult(httpException: HttpException): Result<T, DataError.Network> {
    return when (httpException.code()) {
        408         -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        429         -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else        -> Result.Error(DataError.Network.UNKNOWN)
    }
}