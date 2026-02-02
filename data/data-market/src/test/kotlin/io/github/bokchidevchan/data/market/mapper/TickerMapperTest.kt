package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.TickerDto
import io.github.bokchidevchan.domain.market.entity.Change
import org.junit.Assert.assertEquals
import org.junit.Test

class TickerMapperTest {

    @Test
    fun `toEntity should map TickerDto to Ticker`() {
        val dto = TickerDto(
            market = "KRW-BTC",
            tradePrice = 51000000.0,
            openingPrice = 50000000.0,
            highPrice = 52000000.0,
            lowPrice = 49000000.0,
            prevClosingPrice = 50500000.0,
            change = "RISE",
            changePrice = 500000.0,
            changeRate = 0.0099,
            signedChangePrice = 500000.0,
            signedChangeRate = 0.0099,
            accTradePrice24h = 2000000000.0,
            accTradeVolume24h = 200.0,
            highest52WeekPrice = 70000000.0,
            highest52WeekDate = "2023-01-01",
            lowest52WeekPrice = 30000000.0,
            lowest52WeekDate = "2023-06-01",
            timestamp = 1702641600000
        )

        val entity = dto.toEntity()

        assertEquals("KRW-BTC", entity.market)
        assertEquals(51000000.0, entity.tradePrice, 0.0)
        assertEquals(Change.RISE, entity.change)
        assertEquals(0.0099, entity.changeRate, 0.0001)
    }

    @Test
    fun `toEntity should map FALL change correctly`() {
        val dto = TickerDto(
            market = "KRW-BTC",
            change = "FALL"
        )

        val entity = dto.toEntity()

        assertEquals(Change.FALL, entity.change)
    }

    @Test
    fun `toEntity should map EVEN change correctly`() {
        val dto = TickerDto(
            market = "KRW-BTC",
            change = "EVEN"
        )

        val entity = dto.toEntity()

        assertEquals(Change.EVEN, entity.change)
    }

    @Test
    fun `toEntities should map list of TickerDto to list of Ticker`() {
        val dtos = listOf(
            TickerDto(market = "KRW-BTC"),
            TickerDto(market = "KRW-ETH")
        )

        val entities = dtos.toEntities()

        assertEquals(2, entities.size)
        assertEquals("KRW-BTC", entities[0].market)
        assertEquals("KRW-ETH", entities[1].market)
    }
}
