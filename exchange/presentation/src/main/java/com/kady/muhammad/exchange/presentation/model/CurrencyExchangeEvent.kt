package com.kady.muhammad.exchange.presentation.model

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent.CurrencyExchangeResult
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent.Error

/**
 * Represents events related to currency exchange operations.
 *
 * This sealed interface provides a type-safe way to handle different outcomes
 * of currency exchange transactions, including successful exchanges and errors.
 *
 * @see CurrencyExchangeResult Represents a successful currency exchange
 * @see Error Represents an error during the currency exchange process
 */
sealed interface CurrencyExchangeEvent {
    /**
     * Represents a successful currency exchange transaction.
     *
     * This data class captures the details of a completed currency conversion,
     * including the source and target currencies, and their respective amounts.
     *
     * @property sourceCurrencySymbol The currency symbol of the source amount
     * @property targetCurrencySymbol The currency symbol of the converted amount
     * @property sourceAmount The original amount before conversion
     * @property targetAmount The converted amount after exchange
     */
    data class CurrencyExchangeResult(
        val sourceCurrencySymbol: UiCurrencySymbolModel,
        val targetCurrencySymbol: UiCurrencySymbolModel,
        val sourceAmount: String,
        val targetAmount: String
    ) : CurrencyExchangeEvent

    /**
     * Represents an error that occurred during the currency exchange process.
     *
     * This data class encapsulates network-related errors that may prevent
     * a successful currency exchange transaction.
     *
     * @property error The specific network-related error encountered
     */
    data class Error(
        val error: DataError.Network
    ) : CurrencyExchangeEvent
}