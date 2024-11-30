package com.kady.muhammad.history.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kady.muhammad.history.data.model.DataExchangeHistoryModel
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with the Exchange History table in the database.
 */
@Dao
interface HistoryDao {

    /**
     * Inserts a new exchange history record into the database.
     *
     * @param history The [DataExchangeHistoryModel] object representing the history record to add.
     * This operation is performed as a suspend function, making it safe to call from a coroutine.
     */
    @Insert
    suspend fun add(history: DataExchangeHistoryModel)

    /**
     * Retrieves exchange history records from the last 4 days, ordered by timestamp in descending order.
     *
     * The query uses SQLite's strftime function to compute the Unix timestamp for 4 days ago and
     * compares it with the `timestamp` column (stored in milliseconds). Only records with a timestamp
     * greater than or equal to 4 days ago are included in the result.
     *
     * @return A [Flow] holding a list of [DataExchangeHistoryModel]
     */
    @Query("SELECT * FROM exchange_history WHERE timestamp >= (strftime('%s', 'now', '-4 days') * 1000) ORDER BY timestamp DESC")
    fun getLastFourDaysHistory(): Flow<List<DataExchangeHistoryModel>>
}
