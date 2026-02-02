package io.github.bokchidevchan.feature.market.di

import io.github.bokchidevchan.feature.market.detail.MarketDetailViewModel
import io.github.bokchidevchan.feature.market.list.MarketListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureMarketModule = module {
    viewModel { MarketListViewModel(get(), get()) }
    viewModel { MarketDetailViewModel(get(), get(), get()) }
}
