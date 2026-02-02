package io.github.bokchidevchan.domain.market.repository

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker

interface MarketRepository {
    suspend fun getMarkets(): Result<List<Market>>
    suspend fun getTicker(marketCodes: List<String>): Result<List<Ticker>>
    suspend fun getOrderbook(marketCodes: List<String>): Result<List<Orderbook>>
}
