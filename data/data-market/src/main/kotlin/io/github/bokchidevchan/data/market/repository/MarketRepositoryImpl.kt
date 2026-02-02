package io.github.bokchidevchan.data.market.repository

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.network.safeApiCall
import io.github.bokchidevchan.data.market.api.UpbitApi
import io.github.bokchidevchan.data.market.mapper.toEntities
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val upbitApi: UpbitApi
) : MarketRepository {

    override suspend fun getMarkets(): Result<List<Market>> {
        return safeApiCall {
            upbitApi.getMarkets().toEntities()
        }
    }

    override suspend fun getTicker(marketCodes: List<String>): Result<List<Ticker>> {
        return safeApiCall {
            upbitApi.getTicker(marketCodes.joinToString(",")).toEntities()
        }
    }

    override suspend fun getOrderbook(marketCodes: List<String>): Result<List<Orderbook>> {
        return safeApiCall {
            upbitApi.getOrderbook(marketCodes.joinToString(",")).toEntities()
        }
    }
}
