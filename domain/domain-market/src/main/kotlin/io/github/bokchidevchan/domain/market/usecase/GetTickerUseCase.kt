package io.github.bokchidevchan.domain.market.usecase

import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.entity.Ticker
import io.github.bokchidevchan.domain.market.repository.MarketRepository

class GetTickerUseCase(
    private val marketRepository: MarketRepository
) {
    suspend operator fun invoke(marketCodes: List<String>): Result<List<Ticker>> {
        return marketRepository.getTicker(marketCodes)
    }

    suspend operator fun invoke(marketCode: String): Result<Ticker?> {
        return marketRepository.getTicker(listOf(marketCode)).map { it.firstOrNull() }
    }
}
