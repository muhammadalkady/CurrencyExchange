package com.kady.muhammad.history.domain.model

data class DomainExchangeHistoryModel(
    val sourceSymbol: String,
    val targetSymbol: String,
    val sourceAmount: String,
    val targetAmount: String,
    val timestamp: Long
)
