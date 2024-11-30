package com.kady.muhammad.currencyexchange.di.history

import android.content.Context
import androidx.room.Room
import com.kady.muhammad.history.data.database.HistoryDao
import com.kady.muhammad.history.data.datasource.ExchangeHistoryLocalDataSource
import com.kady.muhammad.history.data.database.HistoryDatabase
import com.kady.muhammad.history.domain.datasource.ExchangeHistoryDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providesHistoryDatabase(
        @ApplicationContext applicationContext: Context
    ): HistoryDatabase = Room.databaseBuilder(
        applicationContext, HistoryDatabase::class.java, "exchange-history-db"
    ).build()

    @Provides
    fun providesHistoryDao(db: HistoryDatabase): HistoryDao = db.historyDao()

    @Provides
    fun providesExchangeHistoryDataSource(historyDao: HistoryDao): ExchangeHistoryDataSource =
        ExchangeHistoryLocalDataSource(historyDao)
}