package io.github.bokchidevchan.core.network.interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflineCacheInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private var isNetworkAvailable = true

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        isNetworkAvailable = true
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `interceptor should not modify request when network is available`() {
        val client = OkHttpClient.Builder()
            .addInterceptor(OfflineCacheInterceptor { isNetworkAvailable })
            .build()

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()

        client.newCall(request).execute()

        val recordedRequest = mockWebServer.takeRequest()
        val cacheControl = recordedRequest.getHeader("Cache-Control")

        assertTrue(cacheControl == null || !cacheControl.contains("max-stale"))
    }

    @Test
    fun `interceptor should add max-stale when network is unavailable`() {
        isNetworkAvailable = false

        val client = OkHttpClient.Builder()
            .addInterceptor(OfflineCacheInterceptor { isNetworkAvailable })
            .build()

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()

        client.newCall(request).execute()

        val recordedRequest = mockWebServer.takeRequest()
        val cacheControl = recordedRequest.getHeader("Cache-Control")

        assertTrue(cacheControl != null)
        assertTrue(cacheControl!!.contains("max-stale"))
    }
}
