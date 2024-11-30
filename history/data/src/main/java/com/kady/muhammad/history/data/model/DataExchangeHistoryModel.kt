package com.kady.muhammad.history.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel

@Entity(tableName = "exchange_history")
data class DataExchangeHistoryModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sourceSymbol: String,
    val targetSymbol: String,
    val sourceAmount: String,
    val targetAmount: String,
    val timestamp: Long,
)

fun DomainExchangeHistoryModel.toData() = DataExchangeHistoryModel(
    sourceSymbol = sourceSymbol,
    targetSymbol = targetSymbol,
    sourceAmount = sourceAmount,
    targetAmount = targetAmount,
    timestamp = timestamp,
)


fun DataExchangeHistoryModel.toDomain() = DomainExchangeHistoryModel(
    sourceSymbol = sourceSymbol,
    targetSymbol = targetSymbol,
    sourceAmount = sourceAmount,
    targetAmount = targetAmount,
    timestamp = timestamp,
)
