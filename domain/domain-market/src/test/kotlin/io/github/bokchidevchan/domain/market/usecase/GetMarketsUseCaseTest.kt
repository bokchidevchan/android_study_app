package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMarketsUseCaseTest {

    private lateinit var marketRepository: MarketRepository
    private lateinit var getMarketsUseCase: GetMarketsUseCase

    private val testMarkets = listOf(
        Market("KRW-BTC", "비트코인", "Bitcoin"),
        Market("KRW-ETH", "이더리움", "Ethereum"),
        Market("BTC-ETH", "이더리움", "Ethereum"),
        Market("USDT-BTC", "비트코인", "Bitcoin")
    )

    @Before
    fun setUp() {
        marketRepository = mockk()
        getMarketsUseCase = GetMarketsUseCase(marketRepository)
    }

    @Test
    fun `필터 없이 호출하면 모든 마켓을 반환해야 한다`() = runTest {
        coEvery { marketRepository.getMarkets() } returns Result.success(testMarkets)

        val result = getMarketsUseCase()

        assertTrue(result.isSuccess)
        assertEquals(4, result.getOrNull()?.size)
    }

    @Test
    fun `KRW 필터로 호출하면 KRW 마켓만 반환해야 한다`() = runTest {
        coEvery { marketRepository.getMarkets() } returns Result.success(testMarkets)

        val result = getMarketsUseCase(MarketType.KRW)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertTrue(result.getOrNull()!!.all { it.code.startsWith("KRW-") })
    }

    @Test
    fun `BTC 필터로 호출하면 BTC 마켓만 반환해야 한다`() = runTest {
        coEvery { marketRepository.getMarkets() } returns Result.success(testMarkets)

        val result = getMarketsUseCase(MarketType.BTC)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertTrue(result.getOrNull()!!.all { it.code.startsWith("BTC-") })
    }

    @Test
    fun `USDT 필터로 호출하면 USDT 마켓만 반환해야 한다`() = runTest {
        coEvery { marketRepository.getMarkets() } returns Result.success(testMarkets)

        val result = getMarketsUseCase(MarketType.USDT)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertTrue(result.getOrNull()!!.all { it.code.startsWith("USDT-") })
    }

    @Test
    fun `호출 시 레포지토리 에러를 전파해야 한다`() = runTest {
        val exception = AppException.NetworkException()
        coEvery { marketRepository.getMarkets() } returns Result.error(exception)

        val result = getMarketsUseCase()

        assertTrue(result.isError)
        assertEquals(exception, result.exceptionOrNull())
    }
}
