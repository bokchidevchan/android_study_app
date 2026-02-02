package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.core.common.toMarketType
import io.github.bokchidevchan.domain.market.entity.Market
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import javax.inject.Inject

class GetMarketsUseCase @Inject constructor(
    private val marketRepository: MarketRepository
) {
    suspend operator fun invoke(marketType: MarketType? = null): Result<List<Market>> {
        return marketRepository.getMarkets().map { markets ->
            if (marketType == null) {
                markets
            } else {
                markets.filter { it.code.toMarketType() == marketType }
            }
        }
    }
}
