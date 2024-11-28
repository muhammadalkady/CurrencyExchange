package com.kady.muhammad.exchange.models

import com.kady.muhammad.exchnage.domain.model.DomainCurrencyModel
import kotlinx.serialization.Serializable

@Serializable
data class DataSymbolsModel(
    val success: Boolean,
    val symbols: Map<String, String>
)

/**
 * Extension function to map [DataSymbolsModel] to a list of [DomainCurrencyModel].
 * It maps the symbols in [DataSymbolsModel] to a collection of [DomainCurrencyModel].
 */
fun DataSymbolsModel.toDomainModel(): List<DomainCurrencyModel> {
    return symbols.map { entry ->
        DomainCurrencyModel(
            name = entry.key, symbol = entry.value,
        )
    }
}