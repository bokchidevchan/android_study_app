package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.OrderbookUnit
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetOrderbookUseCaseTest {

    private lateinit var marketRepository: MarketRepository
    private lateinit var getOrderbookUseCase: GetOrderbookUseCase

    private val testOrderbook = Orderbook(
        market = "KRW-BTC",
        timestamp = 1702641600000,
        totalAskSize = 10.5,
        totalBidSize = 15.2,
        orderbookUnits = listOf(
            OrderbookUnit(
                askPrice = 51100000.0,
                bidPrice = 50900000.0,
                askSize = 1.5,
                bidSize = 2.0
            )
        )
    )

    @Before
    fun setUp() {
        marketRepository = mockk()
        getOrderbookUseCase = GetOrderbookUseCase(marketRepository)
    }

    @Test
    fun `invoke should return orderbook for market`() = runTest {
        coEvery { marketRepository.getOrderbook(listOf("KRW-BTC")) } returns Result.success(listOf(testOrderbook))

        val result = getOrderbookUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertEquals(testOrderbook, result.getOrNull())
    }

    @Test
    fun `invoke should return null when orderbook not found`() = runTest {
        coEvery { marketRepository.getOrderbook(listOf("KRW-BTC")) } returns Result.success(emptyList())

        val result = getOrderbookUseCase("KRW-BTC")

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `invoke should propagate repository error`() = runTest {
        val exception = AppException.NetworkException()
        coEvery { marketRepository.getOrderbook(any()) } returns Result.error(exception)

        val result = getOrderbookUseCase("KRW-BTC")

        assertTrue(result.isError)
        assertEquals(exception, result.exceptionOrNull())
    }
}
