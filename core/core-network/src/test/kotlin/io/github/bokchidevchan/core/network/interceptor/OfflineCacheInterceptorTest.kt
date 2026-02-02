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
    fun `인터셉터는 네트워크가 사용 가능할 때 요청을 수정하지 않아야 한다`() {
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
    fun `인터셉터는 네트워크가 사용 불가능할 때 max-stale을 추가해야 한다`() {
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
