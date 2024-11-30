package com.kady.muhammad.history.model

import androidx.compose.runtime.Immutable
import com.kady.muhammad.core.domain.error.DataError
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class UiExchangeHistoryState(
    val status: UiExchangeHistoryStatus = UiExchangeHistoryStatus.Idle,
    val history: ImmutableMap<String, List<UiExchangeHistoryModel>> = persistentMapOf()
)

@Immutable
sealed interface UiExchangeHistoryStatus {
    data object Idle : UiExchangeHistoryStatus
    data object Loading : UiExchangeHistoryStatus
    data object Loaded : UiExchangeHistoryStatus
    data object LoadedEmpty : UiExchangeHistoryStatus
    data class Error(val error: DataError.Local) : UiExchangeHistoryStatus
}