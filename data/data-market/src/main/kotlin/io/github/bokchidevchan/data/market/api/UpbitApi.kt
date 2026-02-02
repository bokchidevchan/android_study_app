package io.github.bokchidevchan.data.market.api

import io.github.bokchidevchan.data.market.dto.MarketDto
import io.github.bokchidevchan.data.market.dto.OrderbookDto
import io.github.bokchidevchan.data.market.dto.TickerDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UpbitApi {

    @GET("v1/market/all")
    suspend fun getMarkets(): List<MarketDto>

    @GET("v1/ticker")
    suspend fun getTicker(
        @Query("markets") markets: String
    ): List<TickerDto>

    @GET("v1/orderbook")
    suspend fun getOrderbook(
        @Query("markets") markets: String
    ): List<OrderbookDto>
}
