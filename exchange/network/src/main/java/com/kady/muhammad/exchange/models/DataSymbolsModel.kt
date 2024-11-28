package com.kady.muhammad.exchange.models

import kotlinx.serialization.Serializable

@Serializable
data class DataSymbolsModel(
    val success: Boolean,
    val symbols: Map<String, String>
)