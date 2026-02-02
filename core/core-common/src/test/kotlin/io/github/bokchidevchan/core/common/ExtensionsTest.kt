package io.github.bokchidevchan.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `formatPrice는 큰 숫자를 쉼표로 포맷해야 한다`() {
        assertEquals("1,000,000", 1_000_000.0.formatPrice())
        assertEquals("10,000,000", 10_000_000.0.formatPrice())
    }

    @Test
    fun `formatPrice는 백 단위를 소수점 2자리로 포맷해야 한다`() {
        assertEquals("100.00", 100.0.formatPrice())
        assertEquals("999.99", 999.99.formatPrice())
        assertEquals("1,234.56", 1234.56.formatPrice())
    }

    @Test
    fun `formatPrice는 작은 숫자를 소수점 4자리로 포맷해야 한다`() {
        assertEquals("1.0000", 1.0.formatPrice())
        assertEquals("50.1234", 50.1234.formatPrice())
    }

    @Test
    fun `formatPrice는 매우 작은 숫자를 소수점 8자리로 포맷해야 한다`() {
        assertEquals("0.00000001", 0.00000001.formatPrice())
        assertEquals("0.12345678", 0.12345678.formatPrice())
    }

    @Test
    fun `formatPercent는 양수 값을 플러스 기호와 함께 포맷해야 한다`() {
        assertEquals("+5.50%", 5.5.formatPercent())
        assertEquals("+100.00%", 100.0.formatPercent())
    }

    @Test
    fun `formatPercent는 음수 값을 마이너스 기호와 함께 포맷해야 한다`() {
        assertEquals("-5.50%", (-5.5).formatPercent())
        assertEquals("-10.25%", (-10.25).formatPercent())
    }

    @Test
    fun `formatPercent는 0을 포맷해야 한다`() {
        assertEquals("+0.00%", 0.0.formatPercent())
    }

    @Test
    fun `formatVolume은 조 단위를 T 접미사로 포맷해야 한다`() {
        assertEquals("1.50T", 1_500_000_000_000.0.formatVolume())
        assertEquals("2.00T", 2_000_000_000_000.0.formatVolume())
    }

    @Test
    fun `formatVolume은 십억 단위를 B 접미사로 포맷해야 한다`() {
        assertEquals("1.50B", 1_500_000_000.0.formatVolume())
        assertEquals("10.00B", 10_000_000_000.0.formatVolume())
    }

    @Test
    fun `formatVolume은 백만 단위를 M 접미사로 포맷해야 한다`() {
        assertEquals("1.50M", 1_500_000.0.formatVolume())
        assertEquals("999.99M", 999_990_000.0.formatVolume())
    }

    @Test
    fun `formatVolume은 천 단위를 K 접미사로 포맷해야 한다`() {
        assertEquals("1.50K", 1_500.0.formatVolume())
        assertEquals("999.00K", 999_000.0.formatVolume())
    }

    @Test
    fun `formatVolume은 작은 숫자를 접미사 없이 포맷해야 한다`() {
        assertEquals("500.00", 500.0.formatVolume())
        assertEquals("0.00", 0.0.formatVolume())
    }

    @Test
    fun `toMarketType은 KRW 마켓에 대해 KRW를 반환해야 한다`() {
        assertEquals(MarketType.KRW, "KRW-BTC".toMarketType())
        assertEquals(MarketType.KRW, "KRW-ETH".toMarketType())
    }

    @Test
    fun `toMarketType은 BTC 마켓에 대해 BTC를 반환해야 한다`() {
        assertEquals(MarketType.BTC, "BTC-ETH".toMarketType())
        assertEquals(MarketType.BTC, "BTC-XRP".toMarketType())
    }

    @Test
    fun `toMarketType은 USDT 마켓에 대해 USDT를 반환해야 한다`() {
        assertEquals(MarketType.USDT, "USDT-BTC".toMarketType())
        assertEquals(MarketType.USDT, "USDT-ETH".toMarketType())
    }

    @Test
    fun `toMarketType은 알 수 없는 접두사에 대해 UNKNOWN을 반환해야 한다`() {
        assertEquals(MarketType.UNKNOWN, "UNKNOWN-BTC".toMarketType())
        assertEquals(MarketType.UNKNOWN, "TEST".toMarketType())
    }
}
