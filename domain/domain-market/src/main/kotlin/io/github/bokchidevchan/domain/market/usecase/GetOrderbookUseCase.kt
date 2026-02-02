package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Orderbook
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import javax.inject.Inject

class GetOrderbookUseCase @Inject constructor(
    private val marketRepository: MarketRepository
) {
    suspend operator fun invoke(marketCode: String): Result<Orderbook?> {
        return marketRepository.getOrderbook(listOf(marketCode)).map { it.firstOrNull() }
    }
}
