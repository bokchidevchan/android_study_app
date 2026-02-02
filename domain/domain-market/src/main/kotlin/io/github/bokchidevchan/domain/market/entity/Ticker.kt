package io.github.bokchidevchan.domain.market.entity

data class Ticker(
    val market: String,
    val tradePrice: Double,
    val openingPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val prevClosingPrice: Double,
    val change: Change,
    val changePrice: Double,
    val changeRate: Double,
    val signedChangePrice: Double,
    val signedChangeRate: Double,
    val accTradePrice24h: Double,
    val accTradeVolume24h: Double,
    val highest52WeekPrice: Double,
    val highest52WeekDate: String,
    val lowest52WeekPrice: Double,
    val lowest52WeekDate: String,
    val timestamp: Long
)

enum class Change {
    RISE,
    EVEN,
    FALL;

    companion object {
        fun fromString(value: String): Change = when (value.uppercase()) {
            "RISE" -> RISE
            "FALL" -> FALL
            else -> EVEN
        }
    }
}
