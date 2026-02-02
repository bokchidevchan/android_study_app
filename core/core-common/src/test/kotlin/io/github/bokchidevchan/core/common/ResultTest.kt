package io.github.bokchidevchan.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `Success는 데이터를 포함해야 한다`() {
        val result = Result.success("test")

        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertEquals("test", result.getOrNull())
        assertNull(result.exceptionOrNull())
    }

    @Test
    fun `Error는 예외를 포함해야 한다`() {
        val exception = AppException.NetworkException()
        val result = Result.error<String>(exception)

        assertFalse(result.isSuccess)
        assertTrue(result.isError)
        assertNull(result.getOrNull())
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `map은 Success 데이터를 변환해야 한다`() {
        val result = Result.success(10)
        val mapped = result.map { it * 2 }

        assertEquals(20, mapped.getOrNull())
    }

    @Test
    fun `map은 Error를 유지해야 한다`() {
        val exception = AppException.NetworkException()
        val result = Result.error<Int>(exception)
        val mapped = result.map { it * 2 }

        assertTrue(mapped.isError)
        assertEquals(exception, mapped.exceptionOrNull())
    }

    @Test
    fun `flatMap은 Success 결과를 연결해야 한다`() {
        val result = Result.success(10)
        val flatMapped = result.flatMap { Result.success(it * 2) }

        assertEquals(20, flatMapped.getOrNull())
    }

    @Test
    fun `flatMap은 첫 번째 Error를 유지해야 한다`() {
        val exception = AppException.NetworkException()
        val result = Result.error<Int>(exception)
        val flatMapped = result.flatMap { Result.success(it * 2) }

        assertTrue(flatMapped.isError)
        assertEquals(exception, flatMapped.exceptionOrNull())
    }

    @Test
    fun `flatMap은 두 번째 Error를 반환해야 한다`() {
        val result = Result.success(10)
        val exception = AppException.ServerException(500)
        val flatMapped = result.flatMap { Result.error<Int>(exception) }

        assertTrue(flatMapped.isError)
        assertEquals(exception, flatMapped.exceptionOrNull())
    }

    @Test
    fun `onSuccess는 Success일 때 액션을 실행해야 한다`() {
        var executed = false
        val result = Result.success("test")

        result.onSuccess { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onSuccess는 Error일 때 액션을 실행하지 않아야 한다`() {
        var executed = false
        val result = Result.error<String>(AppException.NetworkException())

        result.onSuccess { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `onError는 Error일 때 액션을 실행해야 한다`() {
        var executed = false
        val result = Result.error<String>(AppException.NetworkException())

        result.onError { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onError는 Success일 때 액션을 실행하지 않아야 한다`() {
        var executed = false
        val result = Result.success("test")

        result.onError { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `runCatching은 정상 실행 시 Success를 반환해야 한다`() {
        val result = runCatching { "success" }

        assertTrue(result.isSuccess)
        assertEquals("success", result.getOrNull())
    }

    @Test
    fun `runCatching은 AppException 발생 시 Error를 반환해야 한다`() {
        val result = runCatching<String> {
            throw AppException.NetworkException()
        }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.NetworkException)
    }

    @Test
    fun `runCatching은 다른 예외 발생 시 UnknownException을 반환해야 한다`() {
        val result = runCatching<String> {
            throw IllegalStateException("test error")
        }

        assertTrue(result.isError)
        assertTrue(result.exceptionOrNull() is AppException.UnknownException)
    }
}
