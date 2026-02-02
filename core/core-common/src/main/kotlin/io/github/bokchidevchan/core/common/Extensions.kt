package io.github.bokchidevchan.core.common

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun Double.formatPrice(): String {
    return when {
        this >= 1_000_000 -> {
            val format = DecimalFormat("#,##0")
            format.format(this)
        }
        this >= 100 -> {
            val format = DecimalFormat("#,##0.00")
            format.format(this)
        }
        this >= 1 -> {
            val format = DecimalFormat("#,##0.0000")
            format.format(this)
        }
        else -> {
            val format = DecimalFormat("#,##0.00000000")
            format.format(this)
        }
    }
}

fun Double.formatPercent(): String {
    val format = DecimalFormat("+0.00%;-0.00%")
    return format.format(this / 100)
}

fun Double.formatVolume(): String {
    return when {
        this >= 1_000_000_000_000 -> {
            val format = DecimalFormat("#,##0.00")
            "${format.format(this / 1_000_000_000_000)}T"
        }
        this >= 1_000_000_000 -> {
            val format = DecimalFormat("#,##0.00")
            "${format.format(this / 1_000_000_000)}B"
        }
        this >= 1_000_000 -> {
            val format = DecimalFormat("#,##0.00")
            "${format.format(this / 1_000_000)}M"
        }
        this >= 1_000 -> {
            val format = DecimalFormat("#,##0.00")
            "${format.format(this / 1_000)}K"
        }
        else -> {
            val format = DecimalFormat("#,##0.00")
            format.format(this)
        }
    }
}

fun String.toMarketType(): MarketType {
    return when {
        this.startsWith("KRW-") -> MarketType.KRW
        this.startsWith("BTC-") -> MarketType.BTC
        this.startsWith("USDT-") -> MarketType.USDT
        else -> MarketType.UNKNOWN
    }
}

enum class MarketType {
    KRW, BTC, USDT, UNKNOWN
}
