package com.kady.muhammad.exchange.presentation.model

import androidx.compose.runtime.Immutable
import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.presentation.getFlagEmojiForCurrency
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class UiCurrencyExchangeState(
    val status: UiCurrencyExchangeStatus = UiCurrencyExchangeStatus.Idle,
    val symbols: ImmutableList<UiCurrencySymbolModel> = persistentListOf(),
    val sourceCurrencySymbol: UiCurrencySymbolModel = UiCurrencySymbolModel(
        symbol = "USD",
        flagEmoji = getFlagEmojiForCurrency("USD"),
        description = "United States Dollar"
    ),
    val targetCurrencySymbol: UiCurrencySymbolModel = UiCurrencySymbolModel(
        symbol = "EGP", flagEmoji = getFlagEmojiForCurrency("EGP"), description = "Egyptian Pound"
    ),
    val sourceAmount: String? = "1.0",
    val targetAmount: Double? = null,
    val sourceCurrencyExchangeRate: UiCurrencyExchangeInfoModel? = null,
    val targetCurrencyExchangeRate: UiCurrencyExchangeInfoModel? = null,
)

@Immutable
sealed interface UiCurrencyExchangeStatus {
    data object Idle : UiCurrencyExchangeStatus
    data object Loading : UiCurrencyExchangeStatus
    data object Success : UiCurrencyExchangeStatus
    data class Error(val error: DataError.Network) : UiCurrencyExchangeStatus
}