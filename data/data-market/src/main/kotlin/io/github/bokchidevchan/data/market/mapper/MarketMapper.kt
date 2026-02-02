package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.MarketDto
import io.github.bokchidevchan.domain.market.entity.Market

fun MarketDto.toEntity(): Market = Market(
    code = market,
    koreanName = koreanName,
    englishName = englishName
)

fun List<MarketDto>.toEntities(): List<Market> = map { it.toEntity() }
