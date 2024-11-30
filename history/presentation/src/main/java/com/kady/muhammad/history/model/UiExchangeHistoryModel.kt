package com.kady.muhammad.history.model

import androidx.compose.runtime.Immutable
import com.kady.muhammad.core.presentation.formatTimestampMillis
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel
import java.util.Locale

@Immutable
data class UiExchangeHistoryModel(
    val sourceSymbol: String,
    val targetSymbol: String,
    val sourceAmount: String,
    val targetAmount: String,
    val date: String
)


fun DomainExchangeHistoryModel.toUiModel() = UiExchangeHistoryModel(
    sourceSymbol = sourceSymbol,
    targetSymbol = targetSymbol,
    sourceAmount = String.format(Locale.getDefault(), "%.2f", sourceAmount.toDouble()),
    targetAmount = String.format(Locale.getDefault(), "%.2f", targetAmount.toDouble()),
    date = formatTimestampMillis(timestamp)
)