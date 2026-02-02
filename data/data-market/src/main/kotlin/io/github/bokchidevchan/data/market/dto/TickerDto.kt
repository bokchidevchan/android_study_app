package io.github.bokchidevchan.data.market.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TickerDto(
    @SerialName("market")
    val market: String,
    @SerialName("trade_date")
    val tradeDate: String = "",
    @SerialName("trade_time")
    val tradeTime: String = "",
    @SerialName("trade_date_kst")
    val tradeDateKst: String = "",
    @SerialName("trade_time_kst")
    val tradeTimeKst: String = "",
    @SerialName("trade_timestamp")
    val tradeTimestamp: Long = 0,
    @SerialName("opening_price")
    val openingPrice: Double = 0.0,
    @SerialName("high_price")
    val highPrice: Double = 0.0,
    @SerialName("low_price")
    val lowPrice: Double = 0.0,
    @SerialName("trade_price")
    val tradePrice: Double = 0.0,
    @SerialName("prev_closing_price")
    val prevClosingPrice: Double = 0.0,
    @SerialName("change")
    val change: String = "EVEN",
    @SerialName("change_price")
    val changePrice: Double = 0.0,
    @SerialName("change_rate")
    val changeRate: Double = 0.0,
    @SerialName("signed_change_price")
    val signedChangePrice: Double = 0.0,
    @SerialName("signed_change_rate")
    val signedChangeRate: Double = 0.0,
    @SerialName("trade_volume")
    val tradeVolume: Double = 0.0,
    @SerialName("acc_trade_price")
    val accTradePrice: Double = 0.0,
    @SerialName("acc_trade_price_24h")
    val accTradePrice24h: Double = 0.0,
    @SerialName("acc_trade_volume")
    val accTradeVolume: Double = 0.0,
    @SerialName("acc_trade_volume_24h")
    val accTradeVolume24h: Double = 0.0,
    @SerialName("highest_52_week_price")
    val highest52WeekPrice: Double = 0.0,
    @SerialName("highest_52_week_date")
    val highest52WeekDate: String = "",
    @SerialName("lowest_52_week_price")
    val lowest52WeekPrice: Double = 0.0,
    @SerialName("lowest_52_week_date")
    val lowest52WeekDate: String = "",
    @SerialName("timestamp")
    val timestamp: Long = 0
)
