package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.OrderbookDto
import io.github.bokchidevchan.data.market.dto.OrderbookUnitDto
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderbookMapperTest {

    @Test
    fun `toEntity는 OrderbookDto를 Orderbook으로 매핑해야 한다`() {
        val dto = OrderbookDto(
            market = "KRW-BTC",
            timestamp = 1702641600000,
            totalAskSize = 10.5,
            totalBidSize = 15.2,
            orderbookUnits = listOf(
                OrderbookUnitDto(
                    askPrice = 51100000.0,
                    bidPrice = 50900000.0,
                    askSize = 1.5,
                    bidSize = 2.0
                )
            )
        )

        val entity = dto.toEntity()

        assertEquals("KRW-BTC", entity.market)
        assertEquals(1702641600000, entity.timestamp)
        assertEquals(10.5, entity.totalAskSize, 0.0)
        assertEquals(15.2, entity.totalBidSize, 0.0)
        assertEquals(1, entity.orderbookUnits.size)
    }

    @Test
    fun `toEntity는 OrderbookUnitDto를 올바르게 매핑해야 한다`() {
        val dto = OrderbookUnitDto(
            askPrice = 51100000.0,
            bidPrice = 50900000.0,
            askSize = 1.5,
            bidSize = 2.0
        )

        val entity = dto.toEntity()

        assertEquals(51100000.0, entity.askPrice, 0.0)
        assertEquals(50900000.0, entity.bidPrice, 0.0)
        assertEquals(1.5, entity.askSize, 0.0)
        assertEquals(2.0, entity.bidSize, 0.0)
    }

    @Test
    fun `toEntities는 OrderbookDto 리스트를 Orderbook 리스트로 매핑해야 한다`() {
        val dtos = listOf(
            OrderbookDto(market = "KRW-BTC", timestamp = 1, totalAskSize = 0.0, totalBidSize = 0.0),
            OrderbookDto(market = "KRW-ETH", timestamp = 2, totalAskSize = 0.0, totalBidSize = 0.0)
        )

        val entities = dtos.toEntities()

        assertEquals(2, entities.size)
        assertEquals("KRW-BTC", entities[0].market)
        assertEquals("KRW-ETH", entities[1].market)
    }
}
