package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.common.toMarketType
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class MarketWithTickerData(
    val market: Market,
    val ticker: Ticker? = null
)

class ObserveMarketsWithTickerUseCase @Inject constructor(
    private val marketRepository: MarketRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        marketType: MarketType? = null,
        refreshIntervalMs: Long = 0L
    ): Flow<Result<List<MarketWithTickerData>>> {
        return marketRepository.observeMarkets(refreshIntervalMs)
            .map { result ->
                result.map { markets ->
                    if (marketType == null) markets
                    else markets.filter { it.code.toMarketType() == marketType }
                }
            }
            .flatMapLatest { marketsResult ->
                when (marketsResult) {
                    is Result.Success -> {
                        val markets = marketsResult.data
                        if (markets.isEmpty()) {
                            flowOf(Result.success(emptyList()))
                        } else {
                            val marketCodes = markets.map { it.code }
                            marketRepository.observeTickers(marketCodes, refreshIntervalMs)
                                .map { tickerResult ->
                                    tickerResult.map { tickers ->
                                        val tickerMap = tickers.associateBy { it.market }
                                        markets.map { market ->
                                            MarketWithTickerData(
                                                market = market,
                                                ticker = tickerMap[market.code]
                                            )
                                        }
                                    }
                                }
                        }
                    }
                    is Result.Error -> flowOf(Result.error(marketsResult.exception))
                }
            }
    }
}
