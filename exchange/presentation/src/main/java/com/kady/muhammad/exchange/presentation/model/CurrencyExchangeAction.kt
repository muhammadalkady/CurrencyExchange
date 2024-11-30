package com.kady.muhammad.exchange.presentation.model

/**
 * A sealed interface representing various actions in a currency exchange process.
 * Sealed interfaces allow us to represent a restricted class hierarchy and guarantee
 * that all possible actions are known at compile-time, providing exhaustiveness checks
 * when used in `when` statements or other constructs.
 */
sealed interface CurrencyExchangeAction {

    /**
     * Action to load available currencies.
     * This action does not carry any additional data.
     */
    data object LoadCurrencies : CurrencyExchangeAction

    /**
     * Action to update the source currency.
     *
     * @param symbol The symbol of the source currency represented by [UiCurrencySymbolModel].
     */
    data class UpdateSourceCurrency(val symbol: UiCurrencySymbolModel) : CurrencyExchangeAction

    /**
     * Action to update the target currency.
     *
     * @param symbol The symbol of the target currency represented by [UiCurrencySymbolModel].
     */
    data class UpdateTargetCurrency(val symbol: UiCurrencySymbolModel) : CurrencyExchangeAction

    /**
     * Action to update the amount for the source currency.
     *
     * @param amount The amount entered by the user, which can be null.
     */
    data class UpdateSourceAmount(val amount: String?) : CurrencyExchangeAction

    /**
     * Action to swap the source and target currencies.
     * This action does not carry any additional data.
     */
    data object SwapCurrencies : CurrencyExchangeAction

    /**
     * Action to calculate the exchange rate based on the source amount.
     *
     * @param sourceAmount The amount in the source currency, represented as a [Double].
     */
    data class CalculateExchangeRate(val sourceAmount: Double) : CurrencyExchangeAction
}