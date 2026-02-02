package io.github.bokchidevchan.feature.market.detail

import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.Ticker

data class MarketDetailUiState(
    val isLoading: Boolean = false,
    val marketCode: String = "",
    val ticker: Ticker? = null,
    val orderbook: Orderbook? = null,
    val errorMessage: String? = null
)
