package io.github.bokchidevchan.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppExceptionTest {

    @Test
    fun `NetworkException should have default message`() {
        val exception = AppException.NetworkException()

        assertEquals("Network connection error", exception.message)
        assertNull(exception.cause)
    }

    @Test
    fun `NetworkException should accept custom message and cause`() {
        val cause = RuntimeException("test")
        val exception = AppException.NetworkException("Custom message", cause)

        assertEquals("Custom message", exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `TimeoutException should have default message`() {
        val exception = AppException.TimeoutException()

        assertEquals("Request timed out", exception.message)
        assertNull(exception.cause)
    }

    @Test
    fun `TimeoutException should accept custom message and cause`() {
        val cause = RuntimeException("test")
        val exception = AppException.TimeoutException("Custom timeout", cause)

        assertEquals("Custom timeout", exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `ServerException should contain error code`() {
        val exception = AppException.ServerException(404)

        assertEquals(404, exception.code)
        assertEquals("Server error", exception.message)
    }

    @Test
    fun `ServerException should accept custom message`() {
        val exception = AppException.ServerException(500, "Internal server error")

        assertEquals(500, exception.code)
        assertEquals("Internal server error", exception.message)
    }

    @Test
    fun `UnknownException should have default message`() {
        val exception = AppException.UnknownException()

        assertEquals("Unknown error", exception.message)
        assertNull(exception.cause)
    }

    @Test
    fun `UnknownException should accept custom message and cause`() {
        val cause = RuntimeException("test")
        val exception = AppException.UnknownException("Something went wrong", cause)

        assertEquals("Something went wrong", exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `AppException subtypes should be distinguishable`() {
        val networkException = AppException.NetworkException()
        val timeoutException = AppException.TimeoutException()
        val serverException = AppException.ServerException(500)
        val unknownException = AppException.UnknownException()

        assertTrue(networkException is AppException)
        assertTrue(timeoutException is AppException)
        assertTrue(serverException is AppException)
        assertTrue(unknownException is AppException)

        assertTrue(networkException is AppException.NetworkException)
        assertTrue(timeoutException is AppException.TimeoutException)
        assertTrue(serverException is AppException.ServerException)
        assertTrue(unknownException is AppException.UnknownException)
    }
}
