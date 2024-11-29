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
import com.kady.muhammad.exchange.presentation.model.UiCurrencySymbolModel
import com.kady.muhammad.exchange.presentation.model.toSourceUiModel
import com.kady.muhammad.exchange.presentation.model.toTargetUiModel
import com.kady.muhammad.exchange.presentation.model.toUiModel
import com.kady.muhammad.exchnage.domain.datasource.CurrencyExchangeDataSource
import com.kady.muhammad.exchnage.domain.datasource.DomainCurrencyRateModel
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

/**
 * ViewModel for managing the currency exchange feature.
 *
 * This ViewModel interacts with a data source to fetch currency symbols and exchange rates
 * while maintaining UI state and events. It utilizes the Hilt library for dependency injection
 * and StateFlow for state management.
 *
 * @property currencyExchangeDataSource The data source providing currency-related data.
 */
@HiltViewModel
class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeDataSource: CurrencyExchangeDataSource
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the currency exchange feature.
     *
     * - Initialized with an empty state (`UiCurrencyExchangeState()`).
     * - Automatically triggers the loading of currency symbols when the state flow starts.
     */
    private val _state = MutableStateFlow(UiCurrencyExchangeState())

    /**
     * Public state flow for observing the current UI state.
     *
     * - Shares the state flow in the `viewModelScope` to ensure state emission is tied
     *   to the ViewModel's lifecycle.
     * - Uses `SharingStarted.WhileSubscribed` to emit the latest state to subscribers
     *   while they are active (with a 5-second timeout for late subscribers).
     * - Initialized with the same empty state as `_state`.
     */
    val state = _state.onStart { loadSymbols() }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiCurrencyExchangeState()
    )

    /**
     * A channel for emitting one-time events to the UI, such as error messages.
     *
     * - Uses a `Channel` to ensure events are not lost even if there are no active observers.
     */
    private val _events = Channel<CurrencyExchangeEvent>()

    /**
     * A flow for observing UI events.
     *
     * - Converts the `_events` channel into a flow that can be observed by UI components.
     */
    val events = _events.receiveAsFlow()

    /**
     * Loads the list of available currency symbols from the data source.
     *
     * This function performs the following:
     * 1. Updates the state to indicate that the loading process has started.
     * 2. Fetches the list of currency symbols asynchronously from the `currencyExchangeDataSource`.
     * 3. On a successful fetch:
     *    - Maps the domain models of currency symbols to UI models.
     *    - Updates the UI state with the fetched and mapped symbols.
     * 4. On an error:
     *    - Handles the error using the `handleError` function.
     *
     * This operation is executed within the `viewModelScope` to ensure it is tied
     * to the lifecycle of the `ViewModel` and automatically canceled if the `ViewModel` is cleared.
     */
    private fun loadSymbols() = viewModelScope.launch {
        updateStateLoadingCurrencies()
        currencyExchangeDataSource.getSymbols().onSuccess { symbols ->
            updateStateWithSymbols(
                symbols.map(DomainCurrencyModel::toUiModel).toImmutableList()
            )
        }.onError { handleError(it) }
    }

    /**
     * Loads the exchange rate between two currencies from the data source.
     *
     * This function performs the following:
     * 1. Updates the state to indicate that the exchange rate loading process has started.
     * 2. Asynchronously fetches the exchange rate for the specified source and target currencies
     *    from the `currencyExchangeDataSource`.
     * 3. On a successful fetch:
     *    - Updates the state with the fetched exchange rate and calculates additional derived values
     *      (e.g., converted target amount based on the source amount and exchange rate).
     * 4. On an error:
     *    - Handles the error using the `handleError` function to update the state and notify listeners.
     *
     * The operation is executed within the `viewModelScope` to ensure it respects the lifecycle of
     * the `ViewModel` and is automatically canceled if the `ViewModel` is cleared.
     *
     * @param source The currency symbol of the source currency (e.g., "USD").
     * @param target The currency symbol of the target currency (e.g., "EUR").
     */
    private fun loadExchangeRate(source: String, target: String) = viewModelScope.launch {
        updateStateLoadingExchangeRate()
        currencyExchangeDataSource.getExchangeRate(source, target).onSuccess { rate ->
            updateStateWithExchangeRate(rate)
        }.onError { sendLoadExchangeErrorEvent(it) }
    }


    /**
     * Handles network errors during data operations.
     *
     * This function updates the UI state to reflect the error and sends an error event
     * through the event channel for further handling by listeners.
     *
     * @param error The network error encountered during an operation.
     */
    private suspend fun handleError(error: DataError.Network) {
        _state.update { it.copy(status = UiCurrencyExchangeStatus.Error(error)) }
        _events.send(CurrencyExchangeEvent.Error(error))
    }

    /**
     * Sends an error event when loading the exchange rate fails and resets the state.
     *
     * This function updates the state to reset the status to `LoadedCurrencies`, which signifies
     * the end of the loading operation (even though the exchange rate loading failed). It then
     * sends a network error event to the UI with the provided `DataError.Network` details.
     *
     * @param error The network error to be sent as an event to the UI.
     */
    private suspend fun sendLoadExchangeErrorEvent(error: DataError.Network) {
        _state.update { it.copy(status = UiCurrencyExchangeStatus.LoadedCurrencies) }
        _events.send(CurrencyExchangeEvent.Error(error))
    }

    /**
     * Processes user actions and updates the state or triggers relevant operations.
     *
     * This function acts as the entry point for handling UI interactions by mapping
     * user actions to their corresponding business logic.
     *
     * @param action The user action to process, represented as a sealed class.
     */
    fun onAction(action: CurrencyExchangeAction) {
        when (action) {
            CurrencyExchangeAction.LoadCurrencies           -> loadSymbols()
            is CurrencyExchangeAction.CalculateExchangeRate -> calculateExchangeRate()
            is CurrencyExchangeAction.UpdateSourceCurrency  -> updateSourceCurrency(action.symbol)
            is CurrencyExchangeAction.UpdateTargetCurrency  -> updateTargetCurrency(action.symbol)
            is CurrencyExchangeAction.UpdateSourceAmount    -> updateSourceAmount(action.amount)
            CurrencyExchangeAction.SwapCurrencies           -> swapCurrencies()
        }
    }

    /**
     * Swaps the source and target currencies in the state.
     *
     * This function updates the state to swap the source and target currencies, along with
     * their respective amounts and exchange rates. The source amount is updated to the current
     * target amount, and the target amount is updated based on the current source amount.
     * The corresponding exchange rates and currency symbols are also swapped.
     */
    private fun swapCurrencies() {
        _state.update {
            it.copy(
                // Swap source and target amounts
                sourceAmount = it.targetAmount?.toString() ?: "1.0",
                targetAmount = null,
                // Swap source and target exchange rates
                sourceCurrencyExchangeRate = it.targetCurrencyExchangeRate,
                targetCurrencyExchangeRate = it.sourceCurrencyExchangeRate,
                // Swap source and target currency symbols
                sourceCurrencySymbol = it.targetCurrencySymbol,
                targetCurrencySymbol = it.sourceCurrencySymbol
            )
        }
    }

    /**
     * Updates the UI state to indicate that the list of currencies is being loaded.
     *
     * The `status` field in the state is set to `LoadingCurrencies`, signaling the UI
     * to display a loading indicator specific to the currency list.
     */
    private fun updateStateLoadingCurrencies() {
        _state.update { it.copy(status = UiCurrencyExchangeStatus.LoadingCurrencies) }
    }

    /**
     * Updates the UI state to indicate that the exchange rate is being loaded.
     *
     * The `status` field in the state is set to `LoadingExchangeRate`, signaling the UI
     * to display a loading indicator specific to the exchange rate calculation.
     */
    private fun updateStateLoadingExchangeRate() {
        _state.update { it.copy(status = UiCurrencyExchangeStatus.LoadingExchangeRate) }
    }

    /**
     * Updates the state with the loaded list of currency symbols.
     *
     * This function updates the UI state to indicate that the currency symbols have been successfully
     * loaded by setting the status to `LoadedCurrencies`. It also stores the list of currency symbols
     * in the state.
     *
     * @param symbols A list of UI currency symbols to be stored in the state.
     */
    private fun updateStateWithSymbols(symbols: List<UiCurrencySymbolModel>) {
        _state.update {
            it.copy(
                status = UiCurrencyExchangeStatus.LoadedCurrencies,
                symbols = symbols.toImmutableList()
            )
        }
    }

    /**
     * Updates the state with the loaded exchange rate information.
     *
     * This function updates the UI state to indicate that the exchange rate has been successfully
     * loaded by setting the status to `LoadedExchangeRate`. It also updates the source and target
     * currency exchange rates in the state and calculates the target amount based on the current
     * source amount and the exchange rate.
     *
     * @param rate The loaded exchange rate model to be used in the state update.
     */
    private fun updateStateWithExchangeRate(rate: DomainCurrencyRateModel) {
        _state.update { currentState ->
            val sourceRate = rate.toSourceUiModel()
            val targetRate = rate.toTargetUiModel()
            currentState.copy(
                status = UiCurrencyExchangeStatus.LoadedExchangeRate,
                sourceCurrencyExchangeRate = sourceRate,
                targetCurrencyExchangeRate = targetRate,
                targetAmount = calculateTargetAmount(
                    currentState.sourceAmount, sourceRate.exchangeRate
                )
            )
        }
    }


    /**
     * Calculates the target amount based on the source amount and the exchange rate.
     *
     * This function attempts to convert the source amount from a `String` to a `Double` and
     * multiplies it by the provided exchange rate. If the source amount is invalid or null,
     * it defaults to 0.0.
     *
     * @param sourceAmount The source amount as a string, potentially null or invalid.
     * @param exchangeRate The exchange rate used for the calculation.
     * @return The calculated target amount as a `Double`.
     */
    private fun calculateTargetAmount(sourceAmount: String?, exchangeRate: Double): Double {
        return sourceAmount?.toDoubleOrNull()?.let { amount -> amount * exchangeRate } ?: 0.0
    }

    /**
     * Triggers the exchange rate calculation based on the selected source and target currencies.
     *
     * This function retrieves the current source and target currency symbols from the state and
     * passes them to the `loadExchangeRate` function to fetch the exchange rate.
     */
    private fun calculateExchangeRate() {
        val sourceCurrency = state.value.sourceCurrencySymbol.symbol
        val targetCurrency = state.value.targetCurrencySymbol.symbol
        loadExchangeRate(sourceCurrency, targetCurrency)
    }

    /**
     * Updates the source currency symbol in the state.
     *
     * The selected source currency is set in the `sourceCurrencySymbol` field of the state,
     * allowing the UI to reflect this change.
     *
     * @param symbol The selected source currency as a `UiCurrencySymbolModel`.
     */
    private fun updateSourceCurrency(symbol: UiCurrencySymbolModel) {
        _state.update { it.copy(sourceCurrencySymbol = symbol) }
    }

    /**
     * Updates the target currency symbol in the state.
     *
     * The selected target currency is set in the `targetCurrencySymbol` field of the state,
     * allowing the UI to reflect this change.
     *
     * @param symbol The selected target currency as a `UiCurrencySymbolModel`.
     */
    private fun updateTargetCurrency(symbol: UiCurrencySymbolModel) {
        _state.update { it.copy(targetCurrencySymbol = symbol) }
    }

    /**
     * Updates the source amount and resets the target amount.
     *
     * This function sets the `sourceAmount` field in the state to the provided value and
     * resets the `targetAmount` to null, preparing for a new calculation based on the
     * updated source amount.
     *
     * @param amount The updated source amount as a string, potentially null.
     */
    private fun updateSourceAmount(amount: String?) {
        _state.update { it.copy(sourceAmount = amount, targetAmount = null) }
    }
}
