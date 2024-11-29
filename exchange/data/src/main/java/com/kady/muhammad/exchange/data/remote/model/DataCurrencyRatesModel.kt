package com.kady.muhammad.exchange.data.remote.model

import com.kady.muhammad.exchnage.domain.datasource.DomainCurrencyRateModel
import kotlinx.serialization.Serializable

@Serializable
data class DataCurrencyRatesModel(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)


/**
 * Converts a [DataCurrencyRatesModel] to a [DomainCurrencyRateModel].
 * This function maps the source and target currency rates from a model containing rate data
 * into a domain model that can be used in the app's business logic layer.
 *
 * @return [DomainCurrencyRateModel] the domain model representing the currency rates.
 *
 */
fun DataCurrencyRatesModel.toDomainModel(): DomainCurrencyRateModel {
    val (sourceCurrencySymbol, sourceCurrencyRate) = rates.toList()[0]
    val (targetCurrencySymbol, targetCurrencyRate) = rates.toList()
        .getOrElse(1) { rates.toList()[0] }
    val sourceRate = targetCurrencyRate / sourceCurrencyRate
    val targetRate = sourceCurrencyRate / targetCurrencyRate
    return DomainCurrencyRateModel(
        timestamp = timestamp,
        date = date,
        sourceCurrencySymbol = sourceCurrencySymbol,
        targetCurrencySymbol = targetCurrencySymbol,
        sourceRate = sourceRate,
        targetRate = targetRate
    )
}
