package io.github.bokchidevchan.data.market.repository

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.network.safeApiCall
import io.github.bokchidevchan.data.market.api.UpbitApi
import io.github.bokchidevchan.data.market.mapper.toEntities
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun observeMarkets(refreshIntervalMs: Long): Flow<Result<List<Market>>> = flow {
        emit(getMarkets())
        if (refreshIntervalMs > 0) {
            while (true) {
                delay(refreshIntervalMs)
                emit(getMarkets())
            }
        }
    }

    override fun observeTickers(
        marketCodes: List<String>,
        refreshIntervalMs: Long
    ): Flow<Result<List<Ticker>>> = flow {
        if (marketCodes.isEmpty()) {
            emit(Result.success(emptyList()))
            return@flow
        }
        emit(getTicker(marketCodes))
        if (refreshIntervalMs > 0) {
            while (true) {
                delay(refreshIntervalMs)
                emit(getTicker(marketCodes))
            }
        }
    }
}
