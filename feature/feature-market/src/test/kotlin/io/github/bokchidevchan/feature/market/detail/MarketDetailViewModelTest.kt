package io.github.bokchidevchan.feature.market.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.testing.MainDispatcherRule
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.OrderbookUnit
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.usecase.GetOrderbookUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MarketDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getTickerUseCase: GetTickerUseCase
    private lateinit var getOrderbookUseCase: GetOrderbookUseCase

    private val testTicker = Ticker(
        market = "KRW-BTC",
        tradePrice = 50000000.0,
        openingPrice = 49000000.0,
        highPrice = 51000000.0,
        lowPrice = 48000000.0,
        prevClosingPrice = 49500000.0,
        change = Change.RISE,
        changePrice = 500000.0,
        changeRate = 0.01,
        signedChangePrice = 500000.0,
        signedChangeRate = 0.01,
        accTradePrice24h = 1000000000.0,
        accTradeVolume24h = 100.0,
        highest52WeekPrice = 70000000.0,
        highest52WeekDate = "2023-01-01",
        lowest52WeekPrice = 30000000.0,
        lowest52WeekDate = "2023-06-01",
        timestamp = System.currentTimeMillis()
    )

    private val testOrderbook = Orderbook(
        market = "KRW-BTC",
        timestamp = System.currentTimeMillis(),
        totalAskSize = 10.0,
        totalBidSize = 15.0,
        orderbookUnits = listOf(
            OrderbookUnit(
                askPrice = 50100000.0,
                bidPrice = 49900000.0,
                askSize = 1.0,
                bidSize = 2.0
            )
        )
    )

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle(mapOf("marketCode" to "KRW-BTC"))
        getTickerUseCase = mockk()
        getOrderbookUseCase = mockk()
    }

    @Test
    fun `initial load should fetch ticker and orderbook`() = runTest {
        coEvery { getTickerUseCase(any<String>()) } returns Result.success(testTicker)
        coEvery { getOrderbookUseCase(any()) } returns Result.success(testOrderbook)

        val viewModel = MarketDetailViewModel(savedStateHandle, getTickerUseCase, getOrderbookUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("KRW-BTC", state.marketCode)
            assertNotNull(state.ticker)
            assertNotNull(state.orderbook)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `error should set errorMessage when ticker fails`() = runTest {
        val exception = AppException.NetworkException("Network error")
        coEvery { getTickerUseCase(any<String>()) } returns Result.error(exception)
        coEvery { getOrderbookUseCase(any()) } returns Result.success(testOrderbook)

        val viewModel = MarketDetailViewModel(savedStateHandle, getTickerUseCase, getOrderbookUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Network error", state.errorMessage)
        }
    }

    @Test
    fun `should not show error when only orderbook fails`() = runTest {
        val exception = AppException.NetworkException("Network error")
        coEvery { getTickerUseCase(any<String>()) } returns Result.success(testTicker)
        coEvery { getOrderbookUseCase(any()) } returns Result.error(exception)

        val viewModel = MarketDetailViewModel(savedStateHandle, getTickerUseCase, getOrderbookUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.ticker)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `retry should clear error and reload`() = runTest {
        val exception = AppException.NetworkException("Network error")
        coEvery { getTickerUseCase(any<String>()) } returnsMany listOf(
            Result.error(exception),
            Result.success(testTicker)
        )
        coEvery { getOrderbookUseCase(any()) } returns Result.success(testOrderbook)

        val viewModel = MarketDetailViewModel(savedStateHandle, getTickerUseCase, getOrderbookUseCase)

        viewModel.uiState.test {
            // Initial error state
            var state = awaitItem()
            assertEquals("Network error", state.errorMessage)

            viewModel.retry()

            // After retry - consume items until we find one without loading
            do {
                state = awaitItem()
            } while (state.isLoading)

            assertNull(state.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
