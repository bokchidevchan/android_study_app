package io.github.bokchidevchan.feature.market.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.bokchidevchan.domain.market.usecase.GetOrderbookUseCase
import io.github.bokchidevchan.domain.market.usecase.GetTickerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTickerUseCase: GetTickerUseCase,
    private val getOrderbookUseCase: GetOrderbookUseCase
) : ViewModel() {

    private val marketCode: String = checkNotNull(savedStateHandle["marketCode"])

    private val _uiState = MutableStateFlow(MarketDetailUiState(marketCode = marketCode))
    val uiState: StateFlow<MarketDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() {
        _uiState.update { it.copy(errorMessage = null) }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            var hasError = false
            var errorMessage: String? = null

            getTickerUseCase(marketCode)
                .onSuccess { ticker ->
                    _uiState.update { it.copy(ticker = ticker) }
                }
                .onError { exception ->
                    hasError = true
                    errorMessage = exception.message
                }

            getOrderbookUseCase(marketCode)
                .onSuccess { orderbook ->
                    _uiState.update { it.copy(orderbook = orderbook) }
                }
                .onError { exception ->
                    if (!hasError) {
                        hasError = true
                        errorMessage = exception.message
                    }
                }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = if (hasError && it.ticker == null) errorMessage else null
                )
            }
        }
    }
}
