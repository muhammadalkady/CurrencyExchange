package com.kady.muhammad.history.data.datasource

import android.database.sqlite.SQLiteFullException
import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import com.kady.muhammad.history.data.database.HistoryDao
import com.kady.muhammad.history.data.model.toData
import com.kady.muhammad.history.data.model.toDomain
import com.kady.muhammad.history.domain.datasource.ExchangeHistoryDataSource
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ExchangeHistoryLocalDataSource(
    private val historyDao: HistoryDao
) : ExchangeHistoryDataSource {
    override suspend fun add(exchangeHistory: DomainExchangeHistoryModel): Result<Unit, DataError.Local> {
        return try {
            Result.Success(historyDao.add(exchangeHistory.toData()))
        } catch (e: SQLiteFullException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun getLastFourDaysHistory(): Flow<Result<List<DomainExchangeHistoryModel>, DataError.Local>> {
        return historyDao.getLastFourDaysHistory().map { result -> result.map { it.toDomain() } }
            .map<List<DomainExchangeHistoryModel>, Result<List<DomainExchangeHistoryModel>, DataError.Local>> {
                Result.Success(it)
            }.catch { e ->
            if (e is SQLiteFullException) {
                emit(Result.Error(DataError.Local.DISK_FULL))
            } else {
                emit(Result.Error(DataError.Local.UNKNOWN))
            }
        }
    }
}