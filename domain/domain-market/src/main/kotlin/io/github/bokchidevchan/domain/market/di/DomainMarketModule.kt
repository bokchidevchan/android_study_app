package io.github.bokchidevchan.domain.market.di

import io.github.bokchidevchan.domain.market.usecase.GetMarketsUseCase
import io.github.bokchidevchan.domain.market.usecase.GetOrderbookUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import org.koin.dsl.module

val domainMarketModule = module {
    factory { GetMarketsUseCase(get()) }
    factory { GetTickerUseCase(get()) }
    factory { GetOrderbookUseCase(get()) }
}
