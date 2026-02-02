package io.github.bokchidevchan.feature.market.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.bokchidevchan.feature.market.detail.MarketDetailScreen
import io.github.bokchidevchan.feature.market.list.MarketListScreen

const val MARKET_LIST_ROUTE = "markets"
const val MARKET_DETAIL_ROUTE = "ticker/{marketCode}"

fun NavController.navigateToMarketDetail(marketCode: String) {
    navigate("ticker/$marketCode")
}

fun NavGraphBuilder.marketListScreen(
    onMarketClick: (String) -> Unit
) {
    composable(route = MARKET_LIST_ROUTE) {
        MarketListScreen(onMarketClick = onMarketClick)
    }
}

fun NavGraphBuilder.marketDetailScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = MARKET_DETAIL_ROUTE,
        arguments = listOf(
            navArgument("marketCode") { type = NavType.StringType }
        )
    ) {
        MarketDetailScreen(onBackClick = onBackClick)
    }
}
