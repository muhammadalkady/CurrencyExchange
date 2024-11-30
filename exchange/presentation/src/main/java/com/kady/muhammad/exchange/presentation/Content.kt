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
import com.kady.muhammad.exchange.presentation.model.UiCurrencySymbolModel
import com.kady.muhammad.exchange.presentation.viewmodel.CurrencyExchangeViewModel

/**
 * A composable function that renders a comprehensive currency conversion interface.
 *
 * This component serves as the main entry point for the currency exchange feature,
 * coordinating interactions between the UI and the ViewModel. It handles the entire
 * currency conversion workflow, including currency selection, amount input,
 * exchange rate calculation, and error management.
 *
 * @param modifier Optional [Modifier] to customize the layout and appearance of the component
 * @param viewModel The [CurrencyExchangeViewModel] that manages the business logic
 *                  and state for currency exchange operations
 * @param onCurrencyExchangeResult Callback function to handle the result of a currency exchange
 *
 */
@Composable
fun CurrencyConverter(
    modifier: Modifier = Modifier,
    viewModel: CurrencyExchangeViewModel = hiltViewModel(),
    onCurrencyExchangeResult: (CurrencyExchangeEvent.CurrencyExchangeResult) -> Unit = {}
) {
    // Observe the ViewModel's state and collect it with lifecycle awareness
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle any events emitted by the ViewModel (such as errors)
    HandleViewModelEvents(viewModel, onCurrencyExchangeResult)

    // Render the main currency exchange screen with all interactive elements
    CurrencyExchangeScreen(modifier = modifier,
        state = state,
        onSourceCurrencySelected = { handleSourceCurrencySelection(viewModel, it) },
        onTargetCurrencySelected = { handleTargetCurrencySelection(viewModel, it) },
        calculateExchangeRate = { handleExchangeRateCalculation(viewModel, it) },
        onSwap = { handleCurrencySwap(viewModel) },
        onSourceAmountChanged = { handleSourceAmountUpdate(viewModel, it) },
        onRetry = { handleRetry(viewModel) })
}

/**
 * Handles events emitted by the CurrencyExchangeViewModel, primarily for error management.
 *
 * This composable function observes the ViewModel's event stream and displays
 * toast messages for any error events that occur during currency exchange operations.
 *
 * @param viewModel The ViewModel to observe for events
 * @param onCurrencyExchangeResult Callback function to handle the result of a currency exchange
 */
@Composable
private fun HandleViewModelEvents(
    viewModel: CurrencyExchangeViewModel,
    onCurrencyExchangeResult: (CurrencyExchangeEvent.CurrencyExchangeResult) -> Unit = {}
) {
    val context = LocalContext.current

    // Observe and react to events from the ViewModel
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            // Display a short toast message when an error occurs
            is CurrencyExchangeEvent.Error -> {
                Toast.makeText(context, event.error.toString(context), Toast.LENGTH_SHORT).show()
            }

            is CurrencyExchangeEvent.CurrencyExchangeResult -> onCurrencyExchangeResult(event)
        }
    }
}

/**
 * Handles the selection of the source currency by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the currency selection action
 * @param currency The selected source currency
 */
private fun handleSourceCurrencySelection(
    viewModel: CurrencyExchangeViewModel,
    currency: UiCurrencySymbolModel,
) {
    viewModel.onAction(CurrencyExchangeAction.UpdateSourceCurrency(currency))
}

/**
 * Handles the selection of the target currency by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the currency selection action
 * @param currency The selected target currency
 */
private fun handleTargetCurrencySelection(
    viewModel: CurrencyExchangeViewModel,
    currency: UiCurrencySymbolModel,
) {
    viewModel.onAction(CurrencyExchangeAction.UpdateTargetCurrency(currency))
}

/**
 * Triggers the exchange rate calculation for a given amount by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the calculation action
 * @param amount The amount to be converted
 */
private fun handleExchangeRateCalculation(
    viewModel: CurrencyExchangeViewModel,
    amount: Double,
) {
    viewModel.onAction(CurrencyExchangeAction.CalculateExchangeRate(amount))
}

/**
 * Initiates the currency swap operation by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the swap action
 */
private fun handleCurrencySwap(viewModel: CurrencyExchangeViewModel) {
    viewModel.onAction(CurrencyExchangeAction.SwapCurrencies)
}

/**
 * Handles updates to the source amount by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the amount update action
 * @param amount The new source amount as a string
 */
private fun handleSourceAmountUpdate(
    viewModel: CurrencyExchangeViewModel,
    amount: String,
) {
    viewModel.onAction(CurrencyExchangeAction.UpdateSourceAmount(amount))
}

/**
 * Triggers a retry operation to reload currencies by dispatching an action to the ViewModel.
 *
 * @param viewModel The ViewModel to receive the retry action
 */
private fun handleRetry(viewModel: CurrencyExchangeViewModel) {
    viewModel.onAction(CurrencyExchangeAction.LoadCurrencies)
}