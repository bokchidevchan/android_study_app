package io.github.bokchidevchan.feature.market.list

import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.entity.Ticker

data class MarketListUiState(
    val isLoading: Boolean = false,
    val markets: List<MarketWithTicker> = emptyList(),
    val selectedTab: MarketType = MarketType.KRW,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

data class MarketWithTicker(
    val market: Market,
    val ticker: Ticker? = null
)
