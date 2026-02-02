package io.github.bokchidevchan.data.market.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderbookDto(
    @SerialName("market")
    val market: String,
    @SerialName("timestamp")
    val timestamp: Long = 0,
    @SerialName("total_ask_size")
    val totalAskSize: Double = 0.0,
    @SerialName("total_bid_size")
    val totalBidSize: Double = 0.0,
    @SerialName("orderbook_units")
    val orderbookUnits: List<OrderbookUnitDto> = emptyList()
)

@Serializable
data class OrderbookUnitDto(
    @SerialName("ask_price")
    val askPrice: Double = 0.0,
    @SerialName("bid_price")
    val bidPrice: Double = 0.0,
    @SerialName("ask_size")
    val askSize: Double = 0.0,
    @SerialName("bid_size")
    val bidSize: Double = 0.0
)
