package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.OrderbookDto
import io.github.bokchidevchan.data.market.dto.OrderbookUnitDto
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.entity.OrderbookUnit

fun OrderbookDto.toEntity(): Orderbook = Orderbook(
    market = market,
    timestamp = timestamp,
    totalAskSize = totalAskSize,
    totalBidSize = totalBidSize,
    orderbookUnits = orderbookUnits.map { it.toEntity() }
)

fun OrderbookUnitDto.toEntity(): OrderbookUnit = OrderbookUnit(
    askPrice = askPrice,
    bidPrice = bidPrice,
    askSize = askSize,
    bidSize = bidSize
)

fun List<OrderbookDto>.toEntities(): List<Orderbook> = map { it.toEntity() }
