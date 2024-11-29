package com.kady.muhammad.exchange.presentation.model

import androidx.compose.runtime.Immutable
import com.kady.muhammad.exchnage.domain.datasource.DomainCurrencyRateModel

@Immutable
data class UiCurrencyExchangeInfoModel(
    val symbol: String, val exchangeRate: Double
)


fun DomainCurrencyRateModel.toSourceUiModel(): UiCurrencyExchangeInfoModel {
    return UiCurrencyExchangeInfoModel(
        symbol = sourceCurrencySymbol, exchangeRate = sourceRate
    )
}

fun DomainCurrencyRateModel.toTargetUiModel(): UiCurrencyExchangeInfoModel {
    return UiCurrencyExchangeInfoModel(
        symbol = targetCurrencySymbol, exchangeRate = targetRate
    )
}