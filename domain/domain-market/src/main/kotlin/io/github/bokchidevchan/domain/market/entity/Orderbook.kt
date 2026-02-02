package io.github.bokchidevchan.domain.market.entity

data class Orderbook(
    val market: String,
    val timestamp: Long,
    val totalAskSize: Double,
    val totalBidSize: Double,
    val orderbookUnits: List<OrderbookUnit>
)

data class OrderbookUnit(
    val askPrice: Double,
    val bidPrice: Double,
    val askSize: Double,
    val bidSize: Double
)
