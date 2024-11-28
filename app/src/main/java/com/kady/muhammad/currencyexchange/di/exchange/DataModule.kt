package com.kady.muhammad.currencyexchange.di.exchange

import com.kady.muhammad.exchange.data.remote.CurrencyExchangeRemoteDataSource
import com.kady.muhammad.exchange.network.service.CurrencyExchangeService
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
    fun providesCurrencyExchangeDataSource(service: CurrencyExchangeService): CurrencyExchangeRemoteDataSource =
        CurrencyExchangeRemoteDataSource(service)

}