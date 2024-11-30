package com.kady.muhammad.history.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kady.muhammad.history.model.UiExchangeHistoryModel
import com.kady.muhammad.history.model.UiExchangeHistoryState
import com.kady.muhammad.history.model.UiExchangeHistoryStatus
import com.kady.muhammad.history.presentation.viewmodel.ExchangeHistoryViewModel
import kotlinx.collections.immutable.ImmutableMap

/**
 * A Composable function that displays the exchange history based on the current state
 * provided by the [ExchangeHistoryViewModel].
 *
 * The function handles different states such as:
 * - **Idle** or **Loading**: Displays a loading spinner.
 * - **Loaded**: Displays the history data in a list view.
 * - **LoadedEmpty**: Displays a message indicating no data is available.
 * - **Error**: Displays an error message based on the state.
 *
 * @param modifier An optional [Modifier] that can be applied to the [Box] composable.
 *                 Default is [Modifier.fillMaxSize()].
 * @param viewModel The [ExchangeHistoryViewModel] instance that provides the current state
 *                  of the exchange history, typically containing the status of the data
 *                  request and the historical data.
 */
@Composable
fun ExchangeHistory(
    modifier: Modifier = Modifier,
    viewModel: ExchangeHistoryViewModel,
) {
    // Collecting the state from the view model as a flow with lifecycle-awareness.
    val state: UiExchangeHistoryState by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (state.status) {
            // Show loading state (either idle or in the process of loading)
            UiExchangeHistoryStatus.Idle, UiExchangeHistoryStatus.Loading -> {
                CircularProgressIndicator() // Display loading spinner
            }

            // Show the loaded exchange history list when the data is successfully fetched
            UiExchangeHistoryStatus.Loaded -> {
                UiExchangeHistoryListView(history = state.history)
            }

            // Show a message when no data is available
            UiExchangeHistoryStatus.LoadedEmpty -> {
                Text("No Data Available")
            }

            // Show error message if there was an issue with loading the data
            is UiExchangeHistoryStatus.Error -> {
                Text("Error: ${state.status}")
            }
        }
    }
}

/**
 * A Composable function that displays a list of exchange history grouped by categories.
 * The list is displayed in a [LazyColumn] with each category showing a header (the key),
 * followed by a list of exchange history items.
 *
 * @param history An immutable map where the key represents a category (e.g., date, currency pair),
 *                and the value is a list of [UiExchangeHistoryModel] representing exchange history items.
 */
@Composable
fun UiExchangeHistoryListView(history: ImmutableMap<String, List<UiExchangeHistoryModel>>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)
    ) {
        // Loop through each category in the history map and display the header and the list of items
        history.forEach { (key, historyList) ->
            // Display category header
            item {
                Text(key, style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Display list of history items
            items(historyList) { historyItem ->
                UiExchangeHistoryItemView(historyItem)
                HorizontalDivider()
            }
        }
    }
}

/**
 * A Composable function that displays the details of a single exchange history item.
 * This view includes:
 * - The date of the exchange.
 * - The source amount and symbol.
 * - An arrow indicating the conversion.
 * - The target amount and symbol.
 *
 * @param historyItem A [UiExchangeHistoryModel] representing a single exchange history item to display.
 */
@Composable
fun UiExchangeHistoryItemView(historyItem: UiExchangeHistoryModel) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        // Display the date of the exchange
        Text("Time: ${historyItem.date}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Display the exchange details (source amount, source symbol, conversion arrow, target amount, and target symbol)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Source amount
            Text(
                historyItem.sourceAmount,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            // Source symbol
            Text(
                historyItem.sourceSymbol,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Conversion arrow
            Image(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                modifier = Modifier.height(24.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Target symbol
            Text(
                historyItem.targetSymbol,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            // Target amount
            Text(
                historyItem.targetAmount,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}