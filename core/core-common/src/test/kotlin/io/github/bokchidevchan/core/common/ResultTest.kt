package io.github.bokchidevchan.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `Success should contain data`() {
        val result = Result.success("test")

        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertEquals("test", result.getOrNull())
        assertNull(result.exceptionOrNull())
    }

    @Test
    fun `Error should contain exception`() {
        val exception = AppException.NetworkException()
        val result = Result.error<String>(exception)

        assertFalse(result.isSuccess)
        assertTrue(result.isError)
        assertNull(result.getOrNull())
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `map should transform Success data`() {
        val result = Result.success(10)
        val mapped = result.map { it * 2 }

        assertEquals(20, mapped.getOrNull())
    }

    @Test
    fun `map should preserve Error`() {
        val exception = AppException.NetworkException()
        val result = Result.error<Int>(exception)
        val mapped = result.map { it * 2 }

        assertTrue(mapped.isError)
        assertEquals(exception, mapped.exceptionOrNull())
    }

    @Test
    fun `flatMap should chain Success results`() {
        val result = Result.success(10)
        val flatMapped = result.flatMap { Result.success(it * 2) }

        assertEquals(20, flatMapped.getOrNull())
    }

    @Test
    fun `flatMap should preserve first Error`() {
        val exception = AppException.NetworkException()
        val result = Result.error<Int>(exception)
        val flatMapped = result.flatMap { Result.success(it * 2) }

        assertTrue(flatMapped.isError)
        assertEquals(exception, flatMapped.exceptionOrNull())
    }

    @Test
    fun `flatMap should return second Error`() {
        val result = Result.success(10)
        val exception = AppException.ServerException(500)
        val flatMapped = result.flatMap { Result.error<Int>(exception) }

        assertTrue(flatMapped.isError)
        assertEquals(exception, flatMapped.exceptionOrNull())
    }

    @Test
    fun `onSuccess should execute action for Success`() {
        var executed = false
        val result = Result.success("test")

        result.onSuccess { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onSuccess should not execute action for Error`() {
        var executed = false
        val result = Result.error<String>(AppException.NetworkException())

        result.onSuccess { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `onError should execute action for Error`() {
        var executed = false
        val result = Result.error<String>(AppException.NetworkException())

        result.onError { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onError should not execute action for Success`() {
        var executed = false
        val result = Result.success("test")

        result.onError { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `runCatching should return Success for normal execution`() {
        val result = runCatching { "success" }

        assertTrue(result.isSuccess)
        assertEquals("success", result.getOrNull())
    }

    @Test
    fun `runCatching should return Error for AppException`() {
        val result = runCatching<String> {
            throw AppException.NetworkException()
        }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.NetworkException)
    }

    @Test
    fun `runCatching should return UnknownException for other exceptions`() {
        val result = runCatching<String> {
            throw IllegalStateException("test error")
        }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.UnknownException)
    }
}
