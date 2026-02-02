package io.github.bokchidevchan.data.market.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.bokchidevchan.core.testing.MockWebServerRule
import io.github.bokchidevchan.core.testing.TestData
import io.github.bokchidevchan.data.market.api.UpbitApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class MarketRepositoryImplTest {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    private lateinit var upbitApi: UpbitApi
    private lateinit var repository: MarketRepositoryImpl

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Before
    fun setUp() {
        val contentType = "application/json".toMediaType()
        upbitApi = Retrofit.Builder()
            .baseUrl(mockWebServerRule.baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UpbitApi::class.java)

        repository = MarketRepositoryImpl(upbitApi)
    }

    @Test
    fun `getMarkets should return markets on success`() = runTest {
        mockWebServerRule.mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestData.marketAllResponse)
        )

        val result = repository.getMarkets()

        assertTrue(result.isSuccess)
        assertEquals(4, result.getOrNull()?.size)
        assertEquals("KRW-BTC", result.getOrNull()?.first()?.code)
    }

    @Test
    fun `getMarkets should return error on failure`() = runTest {
        mockWebServerRule.mockWebServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        val result = repository.getMarkets()

        assertTrue(result.isError)
    }

    @Test
    fun `getTicker should return tickers on success`() = runTest {
        mockWebServerRule.mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestData.tickerResponse)
        )

        val result = repository.getTicker(listOf("KRW-BTC"))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("KRW-BTC", result.getOrNull()?.first()?.market)
        assertEquals(51000000.0, result.getOrNull()?.first()?.tradePrice ?: 0.0, 0.0)
    }

    @Test
    fun `getTicker should join multiple market codes`() = runTest {
        mockWebServerRule.mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestData.tickerResponse)
        )

        repository.getTicker(listOf("KRW-BTC", "KRW-ETH"))

        val request = mockWebServerRule.mockWebServer.takeRequest()
        // URL encoding converts comma to %2C
        val path = request.path ?: ""
        assertTrue(path.contains("markets=KRW-BTC") && (path.contains(",KRW-ETH") || path.contains("%2CKRW-ETH")))
    }

    @Test
    fun `getOrderbook should return orderbook on success`() = runTest {
        mockWebServerRule.mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestData.orderbookResponse)
        )

        val result = repository.getOrderbook(listOf("KRW-BTC"))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("KRW-BTC", result.getOrNull()?.first()?.market)
        assertEquals(2, result.getOrNull()?.first()?.orderbookUnits?.size)
    }
}
