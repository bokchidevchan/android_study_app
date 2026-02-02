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
    fun `리스트로 호출하면 모든 마켓의 티커를 반환해야 한다`() = runTest {
        val marketCodes = listOf("KRW-BTC", "KRW-ETH")
        coEvery { marketRepository.getTicker(marketCodes) } returns Result.success(listOf(testTicker))

        val result = getTickerUseCase(marketCodes)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        coVerify { marketRepository.getTicker(marketCodes) }
    }

    @Test
    fun `단일 코드로 호출하면 단일 티커를 반환해야 한다`() = runTest {
        coEvery { marketRepository.getTicker(listOf("KRW-BTC")) } returns Result.success(listOf(testTicker))

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertEquals(testTicker, result.getOrNull())
    }

    @Test
    fun `단일 코드로 호출 시 티커를 찾지 못하면 null을 반환해야 한다`() = runTest {
        coEvery { marketRepository.getTicker(listOf("KRW-BTC")) } returns Result.success(emptyList())

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `호출 시 레포지토리 에러를 전파해야 한다`() = runTest {
        val exception = AppException.NetworkException()
        coEvery { marketRepository.getTicker(any()) } returns Result.error(exception)

        val result = getTickerUseCase("KRW-BTC")

        assertTrue(result.isError)
        assertEquals(exception, result.exceptionOrNull())
    }
}
