package com.kady.muhammad.history.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kady.muhammad.history.data.model.DataExchangeHistoryModel

@Database(entities = [DataExchangeHistoryModel::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}