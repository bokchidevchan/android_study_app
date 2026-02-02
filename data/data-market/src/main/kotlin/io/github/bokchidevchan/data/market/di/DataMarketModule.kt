package io.github.bokchidevchan.data.market.di

import io.github.bokchidevchan.data.market.api.UpbitApi
import io.github.bokchidevchan.data.market.repository.MarketRepositoryImpl
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val dataMarketModule = module {
    single<UpbitApi> {
        get<Retrofit>().create(UpbitApi::class.java)
    }

    single<MarketRepository> {
        MarketRepositoryImpl(get())
    }
}
