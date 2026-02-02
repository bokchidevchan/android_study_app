package io.github.bokchidevchan.feature.market.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.bokchidevchan.core.common.MarketType
import io.github.bokchidevchan.core.common.Result
import io.github.bokchidevchan.domain.market.usecase.GetMarketsUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import io.github.bokchidevchan.domain.market.usecase.ObserveMarketsWithTickerUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketListViewModel @Inject constructor(
    private val getMarketsUseCase: GetMarketsUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val observeMarketsWithTickerUseCase: ObserveMarketsWithTickerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketListUiState())
    val uiState: StateFlow<MarketListUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null
    private var autoRefreshEnabled: Boolean = false

    init {
        loadMarkets()
    }

    fun onTabSelected(marketType: MarketType) {
        _uiState.update { it.copy(selectedTab = marketType) }
        if (autoRefreshEnabled) {
            startObserving()
        } else {
            loadMarkets()
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadMarkets()
    }

    fun retry() {
        _uiState.update { it.copy(errorMessage = null) }
        loadMarkets()
    }

    fun enableAutoRefresh(intervalMs: Long = DEFAULT_REFRESH_INTERVAL_MS) {
        autoRefreshEnabled = true
        startObserving(intervalMs)
    }

    fun disableAutoRefresh() {
        autoRefreshEnabled = false
        observeJob?.cancel()
        observeJob = null
    }

    private fun startObserving(intervalMs: Long = DEFAULT_REFRESH_INTERVAL_MS) {
        observeJob?.cancel()

        val marketType = _uiState.value.selectedTab
        _uiState.update { it.copy(isLoading = true) }

        observeJob = observeMarketsWithTickerUseCase(marketType, intervalMs)
            .onEach { result ->
                handleResult(result)
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = throwable.message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleResult(result: Result<List<io.github.bokchidevchan.domain.market.usecase.MarketWithTickerData>>) {
        when (result) {
            is Result.Success -> {
                val marketsWithTicker = result.data.map { data ->
                    MarketWithTicker(
                        market = data.market,
                        ticker = data.ticker
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
            is Result.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        observeJob?.cancel()
    }

    companion object {
        private const val DEFAULT_REFRESH_INTERVAL_MS = 5000L
    }
}
