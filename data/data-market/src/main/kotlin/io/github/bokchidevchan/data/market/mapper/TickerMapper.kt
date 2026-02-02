package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.TickerDto
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.Ticker

fun TickerDto.toEntity(): Ticker = Ticker(
    market = market,
    tradePrice = tradePrice,
    openingPrice = openingPrice,
    highPrice = highPrice,
    lowPrice = lowPrice,
    prevClosingPrice = prevClosingPrice,
    change = Change.fromString(change),
    changePrice = changePrice,
    changeRate = changeRate,
    signedChangePrice = signedChangePrice,
    signedChangeRate = signedChangeRate,
    accTradePrice24h = accTradePrice24h,
    accTradeVolume24h = accTradeVolume24h,
    highest52WeekPrice = highest52WeekPrice,
    highest52WeekDate = highest52WeekDate,
    lowest52WeekPrice = lowest52WeekPrice,
    lowest52WeekDate = lowest52WeekDate,
    timestamp = timestamp
)

fun List<TickerDto>.toEntities(): List<Ticker> = map { it.toEntity() }
