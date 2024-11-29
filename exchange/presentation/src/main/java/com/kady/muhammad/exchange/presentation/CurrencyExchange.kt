@file:OptIn(ExperimentalMaterial3Api::class)

package com.kady.muhammad.exchange.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    onRetry: (() -> Unit) = { },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        when (state.status) {
            is UiCurrencyExchangeStatus.Error                                              -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.status.error.toString(context = context),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }

            UiCurrencyExchangeStatus.Idle, UiCurrencyExchangeStatus.LoadingCurrencies      -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            UiCurrencyExchangeStatus.Success, UiCurrencyExchangeStatus.LoadingExchangeRate -> {
                CurrencySection(
                    label = "Amount",
                    state.symbols,
                    selectedCurrency = state.sourceCurrencySymbol,
                    onCurrencySelected = onSourceCurrencySelected,
                    amount = state.sourceAmount,
                    onAmountChanged = onSourceAmountChanged,
                    isEditable = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_swap), // Replace with your arrow icon resource
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CurrencySection(
                    label = "Converted Amount",
                    state.symbols,
                    selectedCurrency = state.targetCurrencySymbol,
                    onCurrencySelected = onTargetCurrencySelected,
                    amount = state.targetAmount?.toString() ?: "",
                    isEditable = false
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                    enabled = state.status == UiCurrencyExchangeStatus.Success
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
        }
    }
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
            // Currency Dropdown
            ExposedDropdownMenuBox(expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedCurrency.symbol,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = {
                        TrailingIcon(expanded = expanded)
                    },

                    modifier = Modifier
                        .width(140.dp)
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(text = { Text(text = "${currency.flagEmoji} ${currency.symbol}") },
                            onClick = {
                                expanded = false
                                onCurrencySelected(currency)
                            })
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = amount ?: "",
                onValueChange = onAmountChanged,
                label = { Text("Amount") },
                enabled = isEditable,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

