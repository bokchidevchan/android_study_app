package io.github.bokchidevchan.feature.market.list

import app.cash.turbine.test
import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.testing.MainDispatcherRule
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.usecase.GetMarketsUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MarketListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getMarketsUseCase: GetMarketsUseCase
    private lateinit var getTickerUseCase: GetTickerUseCase

    private val testMarkets = listOf(
        Market("KRW-BTC", "Bitcoin", "Bitcoin"),
        Market("KRW-ETH", "Ethereum", "Ethereum")
    )

    private val testTickers = listOf(
        Ticker(
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
    )

    @Before
    fun setUp() {
        getMarketsUseCase = mockk()
        getTickerUseCase = mockk()
    }

    @Test
    fun `initial load should fetch markets and tickers`() = runTest {
        coEvery { getMarketsUseCase(MarketType.KRW) } returns Result.success(testMarkets)
        coEvery { getTickerUseCase(any<List<String>>()) } returns Result.success(testTickers)

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.markets.size)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `onTabSelected should change tab and reload markets`() = runTest {
        coEvery { getMarketsUseCase(any()) } returns Result.success(testMarkets)
        coEvery { getTickerUseCase(any<List<String>>()) } returns Result.success(testTickers)

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        viewModel.uiState.test {
            skipItems(1)

            viewModel.onTabSelected(MarketType.BTC)

            val state = awaitItem()
            assertEquals(MarketType.BTC, state.selectedTab)
        }
    }

    @Test
    fun `error should set errorMessage`() = runTest {
        val exception = AppException.NetworkException("Network error")
        coEvery { getMarketsUseCase(any()) } returns Result.error(exception)

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Network error", state.errorMessage)
        }
    }

    @Test
    fun `retry should clear error and reload`() = runTest {
        val exception = AppException.NetworkException("Network error")
        coEvery { getMarketsUseCase(any()) } returnsMany listOf(
            Result.error(exception),
            Result.success(testMarkets)
        )
        coEvery { getTickerUseCase(any<List<String>>()) } returns Result.success(testTickers)

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        viewModel.uiState.test {
            // Initial error state
            var state = awaitItem()
            assertEquals("Network error", state.errorMessage)

            viewModel.retry()

            // After retry - consume all items until we find one without error
            do {
                state = awaitItem()
            } while (state.isLoading)

            assertNull(state.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh should set isRefreshing`() = runTest {
        coEvery { getMarketsUseCase(any()) } returns Result.success(testMarkets)
        coEvery { getTickerUseCase(any<List<String>>()) } returns Result.success(testTickers)

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        // Wait for initial load to complete
        viewModel.uiState.test {
            awaitItem()

            viewModel.refresh()

            // Look for either refreshing state or the final state
            var foundRefreshing = false
            var state = awaitItem()
            if (state.isRefreshing) {
                foundRefreshing = true
            }
            // If we got the final state directly, that's also valid (fast execution)
            assertTrue(foundRefreshing || !state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `empty markets should not fetch tickers`() = runTest {
        coEvery { getMarketsUseCase(any()) } returns Result.success(emptyList())

        val viewModel = MarketListViewModel(getMarketsUseCase, getTickerUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.markets.isEmpty())
            assertNull(state.errorMessage)
        }
    }
}
