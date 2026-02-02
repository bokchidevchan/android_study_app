package io.github.bokchidevchan.feature.market.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.bokchidevchan.core.common.formatPercent
import io.github.bokchidevchan.core.common.formatPrice
import io.github.bokchidevchan.core.common.formatVolume
import io.github.bokchidevchan.core.ui.component.ErrorContent
import io.github.bokchidevchan.core.ui.component.LoadingContent
import io.github.bokchidevchan.core.ui.theme.EvenColor
import io.github.bokchidevchan.core.ui.theme.FallColor
import io.github.bokchidevchan.core.ui.theme.FallColorLight
import io.github.bokchidevchan.core.ui.theme.RiseColor
import io.github.bokchidevchan.core.ui.theme.RiseColorLight
import io.github.bokchidevchan.domain.market.entity.Change
import io.github.bokchidevchan.domain.market.entity.OrderbookUnit
import io.github.bokchidevchan.domain.market.entity.Ticker
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDetailScreen(
    onBackClick: () -> Unit,
    viewModel: MarketDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.marketCode) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            uiState.ticker?.let { ticker ->
                                TickerSection(ticker = ticker)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Orderbook",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        uiState.orderbook?.let { orderbook ->
                            items(orderbook.orderbookUnits) { unit ->
                                OrderbookItem(unit = unit)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TickerSection(
    ticker: Ticker,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = ticker.tradePrice.formatPrice(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = getChangeColor(ticker.change)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = (ticker.signedChangeRate * 100).formatPercent(),
                        style = MaterialTheme.typography.titleMedium,
                        color = getChangeColor(ticker.change)
                    )
                    Text(
                        text = "${if (ticker.signedChangePrice >= 0) "+" else ""}${ticker.signedChangePrice.formatPrice()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = getChangeColor(ticker.change)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriceInfo(label = "High", value = ticker.highPrice.formatPrice(), color = RiseColor)
                PriceInfo(label = "Low", value = ticker.lowPrice.formatPrice(), color = FallColor)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriceInfo(label = "Open", value = ticker.openingPrice.formatPrice())
                PriceInfo(label = "Prev Close", value = ticker.prevClosingPrice.formatPrice())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriceInfo(label = "Volume (24h)", value = ticker.accTradeVolume24h.formatVolume())
                PriceInfo(label = "Value (24h)", value = ticker.accTradePrice24h.formatVolume())
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "52 Week High",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticker.highest52WeekPrice.formatPrice(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = RiseColor
                    )
                    Text(
                        text = ticker.highest52WeekDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "52 Week Low",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticker.lowest52WeekPrice.formatPrice(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = FallColor
                    )
                    Text(
                        text = ticker.lowest52WeekDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceInfo(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Composable
private fun OrderbookItem(
    unit: OrderbookUnit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(FallColorLight)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = unit.bidPrice.formatPrice(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = FallColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = unit.bidSize.formatPrice(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .background(RiseColorLight)
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = unit.askPrice.formatPrice(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = RiseColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = unit.askSize.formatPrice(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun getChangeColor(change: Change) = when (change) {
    Change.RISE -> RiseColor
    Change.FALL -> FallColor
    Change.EVEN -> EvenColor
}
