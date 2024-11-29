package com.kady.muhammad.exchange.presentation.model

import com.kady.muhammad.core.domain.error.DataError

sealed interface CurrencyExchangeEvent {
    data class Error(val error: DataError.Network) : CurrencyExchangeEvent
}