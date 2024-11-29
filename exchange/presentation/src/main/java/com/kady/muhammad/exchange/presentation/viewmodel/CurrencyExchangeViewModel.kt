package com.kady.muhammad.exchange.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.onError
import com.kady.muhammad.core.domain.result.onSuccess
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeAction
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent
import com.kady.muhammad.exchange.presentation.model.UiCurrencyExchangeState
import com.kady.muhammad.exchange.presentation.model.UiCurrencyExchangeStatus
import com.kady.muhammad.exchange.presentation.model.toSourceUiModel
import com.kady.muhammad.exchange.presentation.model.toTargetUiModel
import com.kady.muhammad.exchange.presentation.model.toUiModel
import com.kady.muhammad.exchnage.domain.datasource.CurrencyExchangeDataSource
import com.kady.muhammad.exchnage.domain.model.DomainCurrencyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeDataSource: CurrencyExchangeDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(UiCurrencyExchangeState())
    val state = _state.onStart {
        loadSymbols()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiCurrencyExchangeState()
    )

    private val _events = Channel<CurrencyExchangeEvent>()
    val events = _events.receiveAsFlow()

    private fun loadSymbols() {
        viewModelScope.launch {
            _state.update { it.copy(status = UiCurrencyExchangeStatus.Loading) }
            val symbolsResult = currencyExchangeDataSource.getSymbols()
            symbolsResult.onSuccess { symbols ->
                val uiSymbols = symbols.map(DomainCurrencyModel::toUiModel).toImmutableList()
                _state.update {
                    it.copy(
                        status = UiCurrencyExchangeStatus.Success,
                        symbols = uiSymbols,
                    )
                }
            }.onError { error ->
                handleError(error)
            }
        }
    }

    private fun loadExchangeRate(source: String, target: String) {
        viewModelScope.launch {
            _state.update { it.copy(status = UiCurrencyExchangeStatus.Loading) }
            currencyExchangeDataSource.getExchangeRate(source, target).onSuccess { rate ->
                _state.update {
                    it.copy(
                        status = UiCurrencyExchangeStatus.Success,
                        sourceCurrencyExchangeRate = rate.toSourceUiModel(),
                        targetCurrencyExchangeRate = rate.toTargetUiModel(),
                        targetAmount = it.sourceAmount?.toDoubleOrNull()?.let { amount ->
                            amount * rate.toSourceUiModel().exchangeRate
                        } ?: 0.0
                    )
                }
            }.onError { error ->
                handleError(error)
            }
        }
    }

    private suspend fun handleError(error: DataError.Network) {
        _state.update { it.copy(status = UiCurrencyExchangeStatus.Error(error)) }
        _events.send(CurrencyExchangeEvent.Error(error))
    }

    fun onAction(action: CurrencyExchangeAction) {
        when (action) {
            CurrencyExchangeAction.LoadCurrencies           -> loadSymbols()
            is CurrencyExchangeAction.CalculateExchangeRate -> {
                val sourceCurrency = state.value.sourceCurrencySymbol.symbol
                val targetCurrency = state.value.targetCurrencySymbol.symbol
                loadExchangeRate(sourceCurrency, targetCurrency)
            }

            is CurrencyExchangeAction.UpdateSourceCurrency  -> {
                _state.update {
                    it.copy(sourceCurrencySymbol = action.symbol)
                }
            }

            is CurrencyExchangeAction.UpdateTargetCurrency  -> {
                _state.update {
                    it.copy(targetCurrencySymbol = action.symbol)
                }
            }

            is CurrencyExchangeAction.UpdateSourceAmount    -> {
                _state.update {
                    it.copy(sourceAmount = action.amount, targetAmount = null)
                }
            }
        }
    }
}