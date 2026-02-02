package io.github.bokchidevchan.android_study_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.github.bokchidevchan.feature.market.navigation.MARKET_LIST_ROUTE
import io.github.bokchidevchan.feature.market.navigation.marketDetailScreen
import io.github.bokchidevchan.feature.market.navigation.marketListScreen
import io.github.bokchidevchan.feature.market.navigation.navigateToMarketDetail

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MARKET_LIST_ROUTE,
        modifier = modifier
    ) {
        marketListScreen(
            onMarketClick = { marketCode ->
                navController.navigateToMarketDetail(marketCode)
            }
        )

        marketDetailScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}
