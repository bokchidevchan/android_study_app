package io.github.bokchidevchan.core.network.interceptor

import io.github.bokchidevchan.core.network.NetworkConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        client = OkHttpClient.Builder()
            .addNetworkInterceptor(CacheInterceptor())
            .build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `interceptor should add Cache-Control header to response`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"test\": \"data\"}")
        )

        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()

        val response = client.newCall(request).execute()
        val cacheControl = response.header("Cache-Control")

        assertTrue(cacheControl != null)
        assertTrue(cacheControl!!.contains("max-age=${NetworkConfig.CACHE_MAX_AGE_SECONDS}"))
    }

    @Test
    fun `interceptor should remove Pragma header from response`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Pragma", "no-cache")
                .setBody("{\"test\": \"data\"}")
        )

        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()

        val response = client.newCall(request).execute()

        assertEquals(null, response.header("Pragma"))
    }
}
