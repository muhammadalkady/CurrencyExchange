package com.kady.muhammad.exchange.presentation.model

import androidx.compose.runtime.Immutable
import com.kady.muhammad.core.presentation.getFlagEmojiForCurrency
import com.kady.muhammad.exchnage.domain.model.DomainCurrencyModel

@Immutable
data class UiCurrencySymbolModel(
    val symbol: String, val flagEmoji: String, val description: String
)

fun DomainCurrencyModel.toUiModel(): UiCurrencySymbolModel {
    return UiCurrencySymbolModel(
        symbol = symbol, flagEmoji = getFlagEmojiForCurrency(symbol), description = name
    )
}