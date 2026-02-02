package io.github.bokchidevchan.feature.market.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.domain.market.usecase.GetMarketsUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketListViewModel @Inject constructor(
    private val getMarketsUseCase: GetMarketsUseCase,
    private val getTickerUseCase: GetTickerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketListUiState())
    val uiState: StateFlow<MarketListUiState> = _uiState.asStateFlow()

    init {
        loadMarkets()
    }

    fun onTabSelected(marketType: MarketType) {
        _uiState.update { it.copy(selectedTab = marketType) }
        loadMarkets()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadMarkets()
    }

    fun retry() {
        _uiState.update { it.copy(errorMessage = null) }
        loadMarkets()
    }

    private fun loadMarkets() {
        viewModelScope.launch {
            if (!_uiState.value.isRefreshing) {
                _uiState.update { it.copy(isLoading = true) }
            }

            val marketType = _uiState.value.selectedTab

            getMarketsUseCase(marketType)
                .onSuccess { markets ->
                    val marketCodes = markets.map { it.code }

                    if (marketCodes.isNotEmpty()) {
                        getTickerUseCase(marketCodes)
                            .onSuccess { tickers ->
                                val tickerMap = tickers.associateBy { it.market }
                                val marketsWithTicker = markets.map { market ->
                                    MarketWithTicker(
                                        market = market,
                                        ticker = tickerMap[market.code]
                                    )
                                }
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isRefreshing = false,
                                        markets = marketsWithTicker,
                                        errorMessage = null
                                    )
                                }
                            }
                            .onError { exception ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isRefreshing = false,
                                        markets = markets.map { market -> MarketWithTicker(market) },
                                        errorMessage = null
                                    )
                                }
                            }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                markets = emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                }
                .onError { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = exception.message
                        )
                    }
                }
        }
    }
}
