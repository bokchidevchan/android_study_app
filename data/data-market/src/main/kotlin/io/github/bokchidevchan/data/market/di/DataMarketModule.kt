package io.github.bokchidevchan.data.market.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.bokchidevchan.data.market.api.UpbitApi
import io.github.bokchidevchan.data.market.repository.MarketRepositoryImpl
import io.github.bokchidevchan.domain.market.repository.MarketRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataMarketModule {

    @Binds
    @Singleton
    abstract fun bindMarketRepository(
        impl: MarketRepositoryImpl
    ): MarketRepository

    companion object {
        @Provides
        @Singleton
        fun provideUpbitApi(retrofit: Retrofit): UpbitApi {
            return retrofit.create(UpbitApi::class.java)
        }
    }
}
