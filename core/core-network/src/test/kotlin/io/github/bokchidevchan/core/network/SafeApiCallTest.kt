package io.github.bokchidevchan.core.network

import io.github.bokchidevchan.core.common.AppException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SafeApiCallTest {

    @Test
    fun `safeApiCall should return Success for successful call`() = runTest {
        val result = safeApiCall { "success" }

        assertTrue(result.isSuccess)
        assertEquals("success", result.getOrNull())
    }

    @Test
    fun `safeApiCall should return ServerException for HttpException`() = runTest {
        val response = Response.error<String>(404, "Not Found".toResponseBody())
        val result = safeApiCall<String> { throw HttpException(response) }

        assertTrue(result.isError)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AppException.ServerException)
        assertEquals(404, (exception as AppException.ServerException).code)
    }

    @Test
    fun `safeApiCall should return TimeoutException for SocketTimeoutException`() = runTest {
        val result = safeApiCall<String> { throw SocketTimeoutException() }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.TimeoutException)
    }

    @Test
    fun `safeApiCall should return NetworkException for UnknownHostException`() = runTest {
        val result = safeApiCall<String> { throw UnknownHostException() }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.NetworkException)
    }

    @Test
    fun `safeApiCall should return ServerException for SerializationException`() = runTest {
        val result = safeApiCall<String> { throw SerializationException("Parse error") }

        assertTrue(result.isError)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AppException.ServerException)
        assertEquals(0, (exception as AppException.ServerException).code)
        assertTrue(exception.message.contains("Parsing error"))
    }

    @Test
    fun `safeApiCall should return UnknownException for other exceptions`() = runTest {
        val result = safeApiCall<String> { throw IllegalStateException("test") }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.UnknownException)
    }
}
