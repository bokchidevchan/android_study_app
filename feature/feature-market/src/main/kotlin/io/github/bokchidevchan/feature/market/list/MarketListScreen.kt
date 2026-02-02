package io.github.bokchidevchan.feature.market.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.formatPercent
import io.github.bokchidevchan.core.common.formatPrice
import io.github.bokchidevchan.core.common.formatVolume
import io.github.bokchidevchan.core.ui.component.ErrorContent
import io.github.bokchidevchan.core.ui.component.LoadingContent
import io.github.bokchidevchan.core.ui.theme.EvenColor
import io.github.bokchidevchan.core.ui.theme.FallColor
import io.github.bokchidevchan.core.ui.theme.RiseColor
import io.github.bokchidevchan.domain.market.entity.Change
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    onMarketClick: (String) -> Unit,
    viewModel: MarketListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upbit Market") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MarketTabs(
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::onTabSelected
            )

            when {
                uiState.isLoading -> {
                    LoadingContent()
                }
                uiState.errorMessage != null -> {
                    ErrorContent(
                        message = uiState.errorMessage!!,
                        onRetry = viewModel::retry
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = viewModel::refresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (uiState.markets.isEmpty()) {
                            EmptyContent()
                        } else {
                            MarketList(
                                markets = uiState.markets,
                                onMarketClick = onMarketClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MarketTabs(
    selectedTab: MarketType,
    onTabSelected: (MarketType) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(MarketType.KRW, MarketType.BTC, MarketType.USDT)
    val selectedIndex = tabs.indexOf(selectedTab)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
    ) {
        tabs.forEachIndexed { index, marketType ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTabSelected(marketType) },
                text = { Text(marketType.name) }
            )
        }
    }
}

@Composable
private fun MarketList(
    markets: List<MarketWithTicker>,
    onMarketClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = markets,
            key = { it.market.code }
        ) { marketWithTicker ->
            MarketItem(
                marketWithTicker = marketWithTicker,
                onClick = { onMarketClick(marketWithTicker.market.code) }
            )
        }
    }
}

@Composable
private fun MarketItem(
    marketWithTicker: MarketWithTicker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val market = marketWithTicker.market
    val ticker = marketWithTicker.ticker

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = market.koreanName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = market.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (ticker != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = ticker.tradePrice.formatPrice(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = getChangeColor(ticker.change)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = (ticker.signedChangeRate * 100).formatPercent(),
                        style = MaterialTheme.typography.bodySmall,
                        color = getChangeColor(ticker.change)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = ticker.accTradePrice24h.formatVolume(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No markets found",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun getChangeColor(change: Change) = when (change) {
    Change.RISE -> RiseColor
    Change.FALL -> FallColor
    Change.EVEN -> EvenColor
}
