package com.kady.muhammad.exchange.network

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

/**
 * Executes a network call safely and returns a [Result] that encapsulates either the success or error outcome.
 *
 * This function wraps the execution of a network request in a try-catch block, handling various types of exceptions
 * such as network issues, server errors, serialization errors, and others. It ensures that network responses with
 * null bodies are correctly handled and appropriate errors are returned.
 *
 * @param T The type of the response body returned by the network call.
 * @param callProvider A lambda function that provides the [Call] to be executed.
 * @return A [Result] containing the response body in case of success or an error in case of failure.
 */
suspend inline fun <reified T> safeCall(callProvider: () -> Call<T>): Result<T, DataError.Network> {
    return try {
        val retrofitResponse = callProvider().execute()
        if (retrofitResponse.isSuccessful) {
            val body = retrofitResponse.body()
            body?.let {
                Result.Success(it)
            } ?: Result.Error(DataError.Network.SERVER_ERROR)
        } else {
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    } catch (e: UnknownHostException) {
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: IOException) {
        Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: HttpException) {
        mapHttpExceptionToResult(e)
    } catch (e: SerializationException) {
        Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        Result.Error(DataError.Network.UNKNOWN)
    }
}
