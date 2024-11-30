package com.kady.muhammad.history.domain.datasource

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel
import kotlinx.coroutines.flow.Flow

interface ExchangeHistoryDataSource {
    suspend fun add(exchangeHistory: DomainExchangeHistoryModel): Result<Unit, DataError.Local>

    suspend fun getLastFourDaysHistory(): Flow<Result<List<DomainExchangeHistoryModel>, DataError.Local>>
}