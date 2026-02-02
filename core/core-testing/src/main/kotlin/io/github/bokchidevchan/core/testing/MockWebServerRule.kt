package io.github.bokchidevchan.core.testing

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource

class MockWebServerRule : ExternalResource() {

    lateinit var mockWebServer: MockWebServer
        private set

    val baseUrl: String
        get() = mockWebServer.url("/").toString()

    override fun before() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    override fun after() {
        mockWebServer.shutdown()
    }
}
