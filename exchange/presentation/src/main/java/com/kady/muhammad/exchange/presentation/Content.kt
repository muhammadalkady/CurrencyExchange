package com.kady.muhammad.exchange.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kady.muhammad.core.presentation.ObserveAsEvents
import com.kady.muhammad.core.presentation.toString
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeAction
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent
import com.kady.muhammad.exchange.presentation.viewmodel.CurrencyExchangeViewModel

@Composable
fun CurrencyConverter(
    modifier: Modifier = Modifier, viewModel: CurrencyExchangeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CurrencyExchangeEvent.Error -> {
                Toast.makeText(context, event.error.toString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
    CurrencyExchangeScreen(modifier,
        state,
        onSourceCurrencySelected = {
            viewModel.onAction(
                CurrencyExchangeAction.UpdateSourceCurrency(it)
            )
        },
        onTargetCurrencySelected = {
            viewModel.onAction(
                CurrencyExchangeAction.UpdateTargetCurrency(it)
            )
        },
        calculateExchangeRate = {
            viewModel.onAction(
                CurrencyExchangeAction.CalculateExchangeRate(it)
            )
        },
        onSwap = { viewModel.onAction(CurrencyExchangeAction.SwapCurrencies) },
        onSourceAmountChanged = {
            viewModel.onAction(
                CurrencyExchangeAction.UpdateSourceAmount(it)
            )
        },
        onRetry = {
            viewModel.onAction(
                CurrencyExchangeAction.LoadCurrencies
            )
        })
}
