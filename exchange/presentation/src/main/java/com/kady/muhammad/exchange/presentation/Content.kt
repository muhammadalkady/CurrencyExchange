package com.kady.muhammad.exchange.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kady.muhammad.core.presentation.ObserveAsEvents
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeAction
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent
import com.kady.muhammad.exchange.presentation.viewmodel.CurrencyExchangeViewModel

@Composable
fun CurrencyConverter(
    modifier: Modifier = Modifier, viewModel: CurrencyExchangeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CurrencyExchangeEvent.Error -> {}
        }
    }
    CurrencyExchangeScreen(modifier, state, onSourceCurrencySelected = {
        viewModel.onAction(
            CurrencyExchangeAction.UpdateSourceCurrency(it)
        )
    }, onTargetCurrencySelected = {
        viewModel.onAction(
            CurrencyExchangeAction.UpdateTargetCurrency(it)
        )
    }, calculateExchangeRate = {
        viewModel.onAction(
            CurrencyExchangeAction.CalculateExchangeRate(it)
        )
    },
        onSourceAmountChanged = {
            viewModel.onAction(
                CurrencyExchangeAction.UpdateSourceAmount(it)
            )
        }, onRetry = {
            viewModel.onAction(
                CurrencyExchangeAction.LoadCurrencies
            )
        })
}
