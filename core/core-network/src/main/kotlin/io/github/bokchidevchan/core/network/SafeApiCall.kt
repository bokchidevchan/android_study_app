package io.github.bokchidevchan.core.network

import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.Result
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.success(apiCall())
    } catch (e: HttpException) {
        Result.error(
            AppException.ServerException(
                code = e.code(),
                message = e.message(),
                cause = e
            )
        )
    } catch (e: SocketTimeoutException) {
        Result.error(AppException.TimeoutException(cause = e))
    } catch (e: UnknownHostException) {
        Result.error(AppException.NetworkException(cause = e))
    } catch (e: SerializationException) {
        Result.error(AppException.ServerException(code = 0, message = "Parsing error: ${e.message}", cause = e))
    } catch (e: Exception) {
        Result.error(AppException.UnknownException(message = e.message ?: "Unknown error", cause = e))
    }
}
