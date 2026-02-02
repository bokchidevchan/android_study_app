package io.github.bokchidevchan.data.market.mapper

import io.github.bokchidevchan.data.market.dto.MarketDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MarketMapperTest {

    @Test
    fun `toEntity should map MarketDto to Market`() {
        val dto = MarketDto(
            market = "KRW-BTC",
            koreanName = "비트코인",
            englishName = "Bitcoin"
        )

        val entity = dto.toEntity()

        assertEquals("KRW-BTC", entity.code)
        assertEquals("비트코인", entity.koreanName)
        assertEquals("Bitcoin", entity.englishName)
    }

    @Test
    fun `toEntities should map list of MarketDto to list of Market`() {
        val dtos = listOf(
            MarketDto("KRW-BTC", "비트코인", "Bitcoin"),
            MarketDto("KRW-ETH", "이더리움", "Ethereum")
        )

        val entities = dtos.toEntities()

        assertEquals(2, entities.size)
        assertEquals("KRW-BTC", entities[0].code)
        assertEquals("KRW-ETH", entities[1].code)
    }
}
