package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTickerUseCaseTest {

    private lateinit var marketRepository: MarketRepository
    private lateinit var getTickerUseCase: GetTickerUseCase

    private val testTicker = Ticker(
        market = "KRW-BTC",
        tradePrice = 51000000.0,
        openingPrice = 50000000.0,
        highPrice = 52000000.0,
        lowPrice = 49000000.0,
        prevClosingPrice = 50500000.0,
        change = Change.RISE,
        changePrice = 500000.0,
        changeRate = 0.0099,
        signedChangePrice = 500000.0,
        signedChangeRate = 0.0099,
        accTradePrice24h = 2000000000.0,
        accTradeVolume24h = 200.0,
        highest52WeekPrice = 70000000.0,
        highest52WeekDate = "2023-01-01",
        lowest52WeekPrice = 30000000.0,
        lowest52WeekDate = "2023-06-01",
        timestamp = 1702641600000
    )

    @Before
    fun setUp() {
        marketRepository = mockk()
        getTickerUseCase = GetTickerUseCase(marketRepository)
    }

    @Test
    fun `invoke with list should return tickers for all markets`() = runTest {
        val marketCodes = listOf("KRW-BTC", "KRW-ETH")
        coEvery { marketRepository.getTicker(marketCodes) } returns Result.success(listOf(testTicker))

        val result = getTickerUseCase(marketCodes)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        coVerify { marketRepository.getTicker(marketCodes) }
    }

    @Test
    fun `invoke with single code should return single ticker`() = runTest {
        coEvery { marketRepository.getTicker(listOf("KRW-BTC")) } returns Result.success(listOf(testTicker))

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertEquals(testTicker, result.getOrNull())
    }

    @Test
    fun `invoke with single code should return null when ticker not found`() = runTest {
        coEvery { marketRepository.getTicker(listOf("KRW-BTC")) } returns Result.success(emptyList())

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `invoke should propagate repository error`() = runTest {
        val exception = AppException.NetworkException()
        coEvery { marketRepository.getTicker(any()) } returns Result.error(exception)

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isError)
        assertEquals(exception, result.exceptionOrNull())
    }
}
