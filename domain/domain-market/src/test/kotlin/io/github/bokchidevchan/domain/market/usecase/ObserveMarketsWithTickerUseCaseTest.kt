package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.AppException
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ObserveMarketsWithTickerUseCaseTest {

    private lateinit var useCase: ObserveMarketsWithTickerUseCase
    private lateinit var fakeRepository: FakeMarketRepository

    private fun createTicker(market: String, tradePrice: Double) = Ticker(
        market = market,
        tradePrice = tradePrice,
        openingPrice = tradePrice * 0.98,
        highPrice = tradePrice * 1.02,
        lowPrice = tradePrice * 0.95,
        prevClosingPrice = tradePrice * 0.99,
        change = Change.RISE,
        changePrice = tradePrice * 0.01,
        changeRate = 0.01,
        signedChangePrice = tradePrice * 0.01,
        signedChangeRate = 0.01,
        accTradePrice24h = 1000000000.0,
        accTradeVolume24h = 100.0,
        highest52WeekPrice = tradePrice * 1.5,
        highest52WeekDate = "2023-01-01",
        lowest52WeekPrice = tradePrice * 0.5,
        lowest52WeekDate = "2023-06-01",
        timestamp = System.currentTimeMillis()
    )

    @Before
    fun setUp() {
        fakeRepository = FakeMarketRepository()
        useCase = ObserveMarketsWithTickerUseCase(fakeRepository)
    }

    @Test
    fun `마켓과_티커_데이터를_결합하여_방출해야_한다`() = runTest {
        val markets = listOf(
            Market("KRW-BTC", "비트코인", "Bitcoin"),
            Market("KRW-ETH", "이더리움", "Ethereum")
        )
        val tickers = listOf(
            createTicker("KRW-BTC", 51000000.0),
            createTicker("KRW-ETH", 3500000.0)
        )
        fakeRepository.marketsToReturn = Result.success(markets)
        fakeRepository.tickersToReturn = Result.success(tickers)

        val result = useCase(MarketType.KRW, 0L).first()

        assertTrue(result.isSuccess)
        val data = result.getOrNull()!!
        assertEquals(2, data.size)
        assertEquals("KRW-BTC", data[0].market.code)
        assertNotNull(data[0].ticker)
        assertEquals(51000000.0, data[0].ticker!!.tradePrice, 0.0)
    }

    @Test
    fun `마켓_타입으로_필터링해야_한다`() = runTest {
        val markets = listOf(
            Market("KRW-BTC", "비트코인", "Bitcoin"),
            Market("BTC-ETH", "이더리움", "Ethereum")
        )
        fakeRepository.marketsToReturn = Result.success(markets)
        fakeRepository.tickersToReturn = Result.success(emptyList())

        val result = useCase(MarketType.KRW, 0L).first()

        assertTrue(result.isSuccess)
        val data = result.getOrNull()!!
        assertEquals(1, data.size)
        assertEquals("KRW-BTC", data[0].market.code)
    }

    @Test
    fun `마켓_에러_시_에러를_전파해야_한다`() = runTest {
        fakeRepository.marketsToReturn = Result.error(AppException.NetworkException("Network error"))

        val result = useCase(MarketType.KRW, 0L).first()

        assertTrue(result.isError)
    }

    @Test
    fun `빈_마켓_목록_시_빈_리스트를_반환해야_한다`() = runTest {
        fakeRepository.marketsToReturn = Result.success(emptyList())

        val result = useCase(MarketType.KRW, 0L).first()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun `티커_없는_마켓도_포함해야_한다`() = runTest {
        val markets = listOf(
            Market("KRW-BTC", "비트코인", "Bitcoin"),
            Market("KRW-ETH", "이더리움", "Ethereum")
        )
        val tickers = listOf(
            createTicker("KRW-BTC", 51000000.0)
        )
        fakeRepository.marketsToReturn = Result.success(markets)
        fakeRepository.tickersToReturn = Result.success(tickers)

        val result = useCase(MarketType.KRW, 0L).first()

        assertTrue(result.isSuccess)
        val data = result.getOrNull()!!
        assertEquals(2, data.size)
        assertNotNull(data[0].ticker)
        assertNull(data[1].ticker)
    }

    private class FakeMarketRepository : MarketRepository {
        var marketsToReturn: Result<List<Market>> = Result.success(emptyList())
        var tickersToReturn: Result<List<Ticker>> = Result.success(emptyList())

        override suspend fun getMarkets(): Result<List<Market>> = marketsToReturn
        override suspend fun getTicker(marketCodes: List<String>): Result<List<Ticker>> = tickersToReturn
        override suspend fun getOrderbook(marketCodes: List<String>): Result<List<Orderbook>> {
            return Result.success(emptyList())
        }

        override fun observeMarkets(refreshIntervalMs: Long): Flow<Result<List<Market>>> {
            return flowOf(marketsToReturn)
        }

        override fun observeTickers(
            marketCodes: List<String>,
            refreshIntervalMs: Long
        ): Flow<Result<List<Ticker>>> {
            return flowOf(tickersToReturn)
        }
    }
}
