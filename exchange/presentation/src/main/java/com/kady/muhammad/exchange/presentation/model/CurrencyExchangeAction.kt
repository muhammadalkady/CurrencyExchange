package com.kady.muhammad.exchange.presentation.model

sealed interface CurrencyExchangeAction {
    data object LoadCurrencies : CurrencyExchangeAction
    data class UpdateSourceCurrency(val symbol: UiCurrencySymbolModel) : CurrencyExchangeAction
    data class UpdateTargetCurrency(val symbol: UiCurrencySymbolModel) : CurrencyExchangeAction
    data class UpdateSourceAmount(val amount: String?) : CurrencyExchangeAction
    data class CalculateExchangeRate(val sourceAmount: Double) : CurrencyExchangeAction
}