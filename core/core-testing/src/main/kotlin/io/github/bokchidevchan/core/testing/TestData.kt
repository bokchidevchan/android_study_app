package io.github.bokchidevchan.core.testing

object TestData {

    val marketAllResponse = """
        [
            {
                "market": "KRW-BTC",
                "korean_name": "비트코인",
                "english_name": "Bitcoin"
            },
            {
                "market": "KRW-ETH",
                "korean_name": "이더리움",
                "english_name": "Ethereum"
            },
            {
                "market": "BTC-ETH",
                "korean_name": "이더리움",
                "english_name": "Ethereum"
            },
            {
                "market": "USDT-BTC",
                "korean_name": "비트코인",
                "english_name": "Bitcoin"
            }
        ]
    """.trimIndent()

    val tickerResponse = """
        [
            {
                "market": "KRW-BTC",
                "trade_date": "20231215",
                "trade_time": "120000",
                "trade_date_kst": "20231215",
                "trade_time_kst": "210000",
                "trade_timestamp": 1702641600000,
                "opening_price": 50000000.0,
                "high_price": 52000000.0,
                "low_price": 49000000.0,
                "trade_price": 51000000.0,
                "prev_closing_price": 50500000.0,
                "change": "RISE",
                "change_price": 500000.0,
                "change_rate": 0.0099,
                "signed_change_price": 500000.0,
                "signed_change_rate": 0.0099,
                "trade_volume": 0.001,
                "acc_trade_price": 1000000000.0,
                "acc_trade_price_24h": 2000000000.0,
                "acc_trade_volume": 100.0,
                "acc_trade_volume_24h": 200.0,
                "highest_52_week_price": 70000000.0,
                "highest_52_week_date": "2023-01-01",
                "lowest_52_week_price": 30000000.0,
                "lowest_52_week_date": "2023-06-01",
                "timestamp": 1702641600000
            }
        ]
    """.trimIndent()

    val orderbookResponse = """
        [
            {
                "market": "KRW-BTC",
                "timestamp": 1702641600000,
                "total_ask_size": 10.5,
                "total_bid_size": 15.2,
                "orderbook_units": [
                    {
                        "ask_price": 51100000.0,
                        "bid_price": 50900000.0,
                        "ask_size": 1.5,
                        "bid_size": 2.0
                    },
                    {
                        "ask_price": 51200000.0,
                        "bid_price": 50800000.0,
                        "ask_size": 2.0,
                        "bid_size": 3.0
                    }
                ]
            }
        ]
    """.trimIndent()
}
