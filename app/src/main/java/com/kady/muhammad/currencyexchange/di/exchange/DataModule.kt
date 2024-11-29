package com.kady.muhammad.currencyexchange.di.exchange

import com.kady.muhammad.exchange.data.remote.CurrencyExchangeRemoteDataSource
import com.kady.muhammad.exchange.data.remote.service.CurrencyExchangeService
import com.kady.muhammad.exchnage.domain.datasource.CurrencyExchangeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun providesCurrencyExchangeService(retrofit: Retrofit): CurrencyExchangeService =
        retrofit.create(CurrencyExchangeService::class.java)

    @Provides
    fun providesCurrencyExchangeDataSource(service: CurrencyExchangeService): CurrencyExchangeDataSource =
        CurrencyExchangeRemoteDataSource(service)

}