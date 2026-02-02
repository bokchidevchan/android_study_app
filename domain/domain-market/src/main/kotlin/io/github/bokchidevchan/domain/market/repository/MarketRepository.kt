package io.github.bokchidevchan.domain.market.repository

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    // 일회성 요청 (One-shot requests)
    suspend fun getMarkets(): Result<List<Market>>
    suspend fun getTicker(marketCodes: List<String>): Result<List<Ticker>>
    suspend fun getOrderbook(marketCodes: List<String>): Result<List<Orderbook>>

    // 스트림 관찰 (Stream observation for extensibility)
    fun observeMarkets(refreshIntervalMs: Long = 0L): Flow<Result<List<Market>>>
    fun observeTickers(marketCodes: List<String>, refreshIntervalMs: Long = 0L): Flow<Result<List<Ticker>>>
}
