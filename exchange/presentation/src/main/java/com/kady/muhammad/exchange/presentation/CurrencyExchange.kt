@file:OptIn(ExperimentalMaterial3Api::class)

package com.kady.muhammad.exchange.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kady.muhammad.core.presentation.toString
import com.kady.muhammad.exchange.presentation.model.UiCurrencyExchangeState
import com.kady.muhammad.exchange.presentation.model.UiCurrencyExchangeStatus
import com.kady.muhammad.exchange.presentation.model.UiCurrencySymbolModel

@Composable
fun CurrencyExchangeScreen(
    modifier: Modifier = Modifier,
    state: UiCurrencyExchangeState,
    onSourceCurrencySelected: (UiCurrencySymbolModel) -> Unit = {},
    onTargetCurrencySelected: (UiCurrencySymbolModel) -> Unit = {},
    onSourceAmountChanged: (String) -> Unit = {},
    calculateExchangeRate: (Double) -> Unit = {},
    onSwap: () -> Unit = {},
    onRetry: (() -> Unit) = { },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (state.status) {
            is UiCurrencyExchangeStatus.Error -> {
                CurrencyExchangeError(
                    errorMessage = state.status.error.toString(LocalContext.current),
                    onRetry = onRetry
                )
            }

            UiCurrencyExchangeStatus.Idle, UiCurrencyExchangeStatus.LoadingCurrencies -> {
                CurrencyExchangeLoading()
            }

            UiCurrencyExchangeStatus.LoadedCurrencies,
            UiCurrencyExchangeStatus.LoadingExchangeRate,
            UiCurrencyExchangeStatus.LoadedExchangeRate -> {
                CurrencyExchangeContent(
                    state = state,
                    onSourceCurrencySelected = onSourceCurrencySelected,
                    onTargetCurrencySelected = onTargetCurrencySelected,
                    onSourceAmountChanged = onSourceAmountChanged,
                    onSwap = onSwap,
                    calculateExchangeRate = calculateExchangeRate
                )
            }
        }
    }
}

@Composable
fun CurrencyExchangeError(
    errorMessage: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("RETRY") // Button text
        }
    }
}

@Composable
fun CurrencyExchangeLoading() {
    // Box layout for centering the spinner in the available space
    Box(
        modifier = Modifier.fillMaxSize(), // Fill the entire available space
        contentAlignment = Alignment.Center, // Center the content inside the Box
    ) {
        CircularProgressIndicator() // Display a circular progress indicator (spinner)
    }
}

@Composable
fun ColumnScope.CurrencyExchangeContent(
    state: UiCurrencyExchangeState,
    onSourceCurrencySelected: (UiCurrencySymbolModel) -> Unit,
    onTargetCurrencySelected: (UiCurrencySymbolModel) -> Unit,
    onSourceAmountChanged: (String) -> Unit,
    onSwap: () -> Unit,
    calculateExchangeRate: (Double) -> Unit
) {
    CurrencySection(
        label = "Amount",
        currencies = state.symbols,
        selectedCurrency = state.sourceCurrencySymbol,
        onCurrencySelected = onSourceCurrencySelected,
        amount = state.sourceAmount,
        onAmountChanged = onSourceAmountChanged,
        isEditable = true
    )
    Spacer(modifier = Modifier.height(16.dp))

    Image(
        painter = painterResource(id = R.drawable.ic_swap),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onSwap)
            .background(MaterialTheme.colorScheme.primary)
            .align(Alignment.CenterHorizontally)
            .size(64.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    CurrencySection(
        label = "Converted Amount",
        currencies = state.symbols,
        selectedCurrency = state.targetCurrencySymbol,
        onCurrencySelected = onTargetCurrencySelected,
        amount = state.targetAmount?.toString() ?: "",
        isEditable = false
    )

    Spacer(modifier = Modifier.height(24.dp))

    CalculateButton(
        state = state, calculateExchangeRate = calculateExchangeRate
    )
}

@Composable
fun ColumnScope.CalculateButton(
    state: UiCurrencyExchangeState, calculateExchangeRate: (Double) -> Unit
) {
    Button(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .animateContentSize(),
        onClick = {
            val sourceAmount = state.sourceAmount?.toDoubleOrNull()
            if (sourceAmount != null) {
                calculateExchangeRate(sourceAmount)
            }
        },
        enabled = state.status == UiCurrencyExchangeStatus.LoadedExchangeRate || state.status == UiCurrencyExchangeStatus.LoadedCurrencies
    ) {
        Row {
            Text("CALCULATE")
            if (state.status == UiCurrencyExchangeStatus.LoadingExchangeRate) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    strokeWidth = 1.dp, modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CurrencyDropdown(
    selectedCurrency: UiCurrencySymbolModel,
    currencies: List<UiCurrencySymbolModel>,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onCurrencySelected: (UiCurrencySymbolModel) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
    ) {
        OutlinedTextField(
            value = selectedCurrency.symbol,
            onValueChange = {},
            readOnly = true,
            label = { Text("Currency") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = modifier
                .width(140.dp)
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            currencies.forEach { currency ->
                DropdownMenuItem(text = { Text(text = "${currency.flagEmoji} ${currency.symbol}") },
                    onClick = {
                        onExpandedChange(false)
                        onCurrencySelected(currency)
                    })
            }
        }
    }
}

@Composable
fun AmountInput(
    amount: String?,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    onAmountChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = amount ?: "",
        onValueChange = onAmountChanged,
        label = { Text("Amount") },
        enabled = isEditable,
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = modifier,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun CurrencySection(
    label: String,
    currencies: List<UiCurrencySymbolModel>,
    selectedCurrency: UiCurrencySymbolModel,
    modifier: Modifier = Modifier,
    amount: String? = null,
    onAmountChanged: (String) -> Unit = {},
    onCurrencySelected: (UiCurrencySymbolModel) -> Unit = {},
    isEditable: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CurrencyDropdown(
                selectedCurrency = selectedCurrency,
                currencies = currencies,
                expanded = expanded,
                onCurrencySelected = onCurrencySelected,
                onExpandedChange = { expanded = it },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            AmountInput(
                amount = amount,
                onAmountChanged = onAmountChanged,
                isEditable = isEditable,
                modifier = Modifier.weight(1f)
            )
        }
    }
}