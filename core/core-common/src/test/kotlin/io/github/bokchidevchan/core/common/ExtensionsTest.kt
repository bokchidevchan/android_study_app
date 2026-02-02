package io.github.bokchidevchan.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `formatPrice should format large numbers with commas`() {
        assertEquals("1,000,000", 1_000_000.0.formatPrice())
        assertEquals("10,000,000", 10_000_000.0.formatPrice())
    }

    @Test
    fun `formatPrice should format hundreds with two decimals`() {
        assertEquals("100.00", 100.0.formatPrice())
        assertEquals("999.99", 999.99.formatPrice())
        assertEquals("1,234.56", 1234.56.formatPrice())
    }

    @Test
    fun `formatPrice should format small numbers with four decimals`() {
        assertEquals("1.0000", 1.0.formatPrice())
        assertEquals("50.1234", 50.1234.formatPrice())
    }

    @Test
    fun `formatPrice should format very small numbers with eight decimals`() {
        assertEquals("0.00000001", 0.00000001.formatPrice())
        assertEquals("0.12345678", 0.12345678.formatPrice())
    }

    @Test
    fun `formatPercent should format positive values with plus sign`() {
        assertEquals("+5.50%", 5.5.formatPercent())
        assertEquals("+100.00%", 100.0.formatPercent())
    }

    @Test
    fun `formatPercent should format negative values with minus sign`() {
        assertEquals("-5.50%", (-5.5).formatPercent())
        assertEquals("-10.25%", (-10.25).formatPercent())
    }

    @Test
    fun `formatPercent should format zero`() {
        assertEquals("+0.00%", 0.0.formatPercent())
    }

    @Test
    fun `formatVolume should format trillions with T suffix`() {
        assertEquals("1.50T", 1_500_000_000_000.0.formatVolume())
        assertEquals("2.00T", 2_000_000_000_000.0.formatVolume())
    }

    @Test
    fun `formatVolume should format billions with B suffix`() {
        assertEquals("1.50B", 1_500_000_000.0.formatVolume())
        assertEquals("10.00B", 10_000_000_000.0.formatVolume())
    }

    @Test
    fun `formatVolume should format millions with M suffix`() {
        assertEquals("1.50M", 1_500_000.0.formatVolume())
        assertEquals("999.99M", 999_990_000.0.formatVolume())
    }

    @Test
    fun `formatVolume should format thousands with K suffix`() {
        assertEquals("1.50K", 1_500.0.formatVolume())
        assertEquals("999.00K", 999_000.0.formatVolume())
    }

    @Test
    fun `formatVolume should format small numbers without suffix`() {
        assertEquals("500.00", 500.0.formatVolume())
        assertEquals("0.00", 0.0.formatVolume())
    }

    @Test
    fun `toMarketType should return KRW for KRW markets`() {
        assertEquals(MarketType.KRW, "KRW-BTC".toMarketType())
        assertEquals(MarketType.KRW, "KRW-ETH".toMarketType())
    }

    @Test
    fun `toMarketType should return BTC for BTC markets`() {
        assertEquals(MarketType.BTC, "BTC-ETH".toMarketType())
        assertEquals(MarketType.BTC, "BTC-XRP".toMarketType())
    }

    @Test
    fun `toMarketType should return USDT for USDT markets`() {
        assertEquals(MarketType.USDT, "USDT-BTC".toMarketType())
        assertEquals(MarketType.USDT, "USDT-ETH".toMarketType())
    }

    @Test
    fun `toMarketType should return UNKNOWN for unknown prefixes`() {
        assertEquals(MarketType.UNKNOWN, "UNKNOWN-BTC".toMarketType())
        assertEquals(MarketType.UNKNOWN, "TEST".toMarketType())
    }
}
