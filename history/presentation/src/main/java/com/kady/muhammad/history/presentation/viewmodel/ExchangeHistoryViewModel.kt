package com.kady.muhammad.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.onError
import com.kady.muhammad.core.domain.result.onSuccess
import com.kady.muhammad.core.presentation.groupByDay
import com.kady.muhammad.history.domain.datasource.ExchangeHistoryDataSource
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel
import com.kady.muhammad.history.model.UiExchangeHistoryModel
import com.kady.muhammad.history.model.UiExchangeHistoryState
import com.kady.muhammad.history.model.UiExchangeHistoryStatus
import com.kady.muhammad.history.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the exchange history in the app. This ViewModel interacts with the `ExchangeHistoryDataSource`
 * to fetch, add, and manage the exchange history data. It exposes a state flow (`state`) that the UI observes to reflect
 * the latest UI state, including loading, success, and error states. The ViewModel leverages Kotlin Coroutines for asynchronous
 * operations and Hilt for dependency injection.
 *
 * @param exchangeHistoryDataSource The data source that provides exchange history data.
 */
@HiltViewModel
class ExchangeHistoryViewModel @Inject constructor(
    private val exchangeHistoryDataSource: ExchangeHistoryDataSource
) : ViewModel() {

    // _state is a MutableStateFlow that holds the current state of the UI for the exchange history.
    // It starts by loading the exchange history on initialization.
    private val _state = MutableStateFlow(UiExchangeHistoryState())

    // `state` is a public immutable property that exposes the UI state to the UI layer.
    // It initializes by calling `loadHistory()` and keeps the state updated through `stateIn`.
    val state = _state.onStart {
        loadHistory() // Initiates loading of the exchange history when the ViewModel is created
    }.stateIn(
        viewModelScope, // Tied to the ViewModel's lifecycle
        started = SharingStarted.WhileSubscribed(5000), // Keeps the state alive for 5 seconds after the last collector unsubscribes
        initialValue = UiExchangeHistoryState() // Initial state value when no data is loaded
    )

    /**
     * Adds a new exchange history record to the data source.
     *
     * @param exchangeHistory The exchange history model to be added.
     */
    fun add(exchangeHistory: DomainExchangeHistoryModel) {
        // Launches a coroutine in the ViewModel's scope to perform the add operation
        viewModelScope.launch {
            // Attempts to add the new exchange history, handling success and error cases
            exchangeHistoryDataSource.add(exchangeHistory).onSuccess {
                // No operation on success (no UI updates or side-effects)
            }.onError {
                // Handle errors if needed, e.g., logging
            }
        }
    }

    /**
     * Loads the exchange history for the last four days from the data source.
     * Updates the UI state based on the result of the data fetching.
     */
    private fun loadHistory() {
        // Launches a coroutine to fetch the exchange history asynchronously
        viewModelScope.launch {
            setLoadingState() // Set the UI state to "Loading"

            // Fetches the last four days of exchange history
            exchangeHistoryDataSource.getLastFourDaysHistory()
                .collect {
                    it.onSuccess { result ->
                        // Groups the history by day and updates the state
                        val groupedHistory = groupHistoryByDay(result)
                        updateStateOnSuccess(result, groupedHistory)
                    }.onError { error ->
                        updateStateOnError(error)
                    }
                }
        }
    }

    /**
     * Sets the UI state to "Loading", indicating that data is being fetched.
     */
    private fun setLoadingState() {
        _state.value = UiExchangeHistoryState(status = UiExchangeHistoryStatus.Loading)
    }

    /**
     * Groups the provided exchange history data by day.
     *
     * @param result The list of exchange history models to group.
     * @return A map where the keys are day strings and the values are lists of UI models for that day.
     */
    private fun groupHistoryByDay(result: List<DomainExchangeHistoryModel>): Map<String, List<UiExchangeHistoryModel>> {
        // Groups the result by day using the timestamp as the key and converts the models to UI models
        return groupByDay(result, { it.timestamp }).mapValues { entry ->
            entry.value.map { it.toUiModel() }
        }
    }

    /**
     * Updates the UI state when the data is successfully loaded.
     *
     * @param result The fetched exchange history data.
     * @param groupedHistory The exchange history data grouped by day.
     */
    private fun updateStateOnSuccess(
        result: List<DomainExchangeHistoryModel>,
        groupedHistory: Map<String, List<UiExchangeHistoryModel>>
    ) {
        // Determines the UI status based on whether the result is empty or not
        val status = if (result.isEmpty()) {
            UiExchangeHistoryStatus.LoadedEmpty
        } else {
            UiExchangeHistoryStatus.Loaded
        }

        // Updates the UI state with the new status and grouped history
        _state.value = UiExchangeHistoryState(
            status = status,
            history = groupedHistory.toImmutableMap() // Use ImmutableMap to ensure immutability
        )
    }

    /**
     * Updates the UI state when an error occurs while fetching data.
     *
     * @param error The error that occurred during the data fetch.
     */
    private fun updateStateOnError(error: DataError.Local) {
        _state.value = UiExchangeHistoryState(status = UiExchangeHistoryStatus.Error(error))
    }
}
