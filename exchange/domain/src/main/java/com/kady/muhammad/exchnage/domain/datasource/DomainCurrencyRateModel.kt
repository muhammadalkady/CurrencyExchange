package com.kady.muhammad.exchnage.domain.datasource

/**
 * A domain representation of currency exchange rate information.
 *
 * This data class encapsulates all the necessary information about currency rates between
 * two currencies, along with a timestamp and date for tracking when the rates were obtained.
 * It is used in the business logic layer to handle currency conversion-related operations.
 *
 * @property timestamp The Unix timestamp representing when the currency rates were retrieved.
 *                      Typically used for tracking or caching purposes.
 * @property date A human-readable date string in the format "YYYY-MM-DD" representing the date
 *                the currency rates were recorded.
 * @property sourceCurrencySymbol The ISO 4217 code (e.g., "USD", "EUR") of the source currency.
 *                                This represents the base currency in the conversion.
 * @property targetCurrencySymbol The ISO 4217 code of the target currency.
 *                                This represents the currency to which conversion is made.
 * @property sourceRate The conversion rate from the source currency to the target currency.
 *                      For example, if 1 USD equals 0.85 EUR, the sourceRate would be 0.85.
 * @property targetRate The conversion rate from the target currency to the source currency.
 *                      For example, if 1 EUR equals 1.18 USD, the targetRate would be 1.18.
 */
data class DomainCurrencyRateModel(
    val timestamp: Long,
    val date: String,
    val sourceCurrencySymbol: String,
    val targetCurrencySymbol: String,
    val sourceRate: Double,
    val targetRate: Double
)
