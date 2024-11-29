package com.kady.muhammad.exchange.data.remote.service

import com.kady.muhammad.exchange.data.remote.model.DataCurrencyRatesModel
import com.kady.muhammad.exchange.data.remote.model.DataSymbolsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyExchangeService {
    @GET("symbols")
    fun getSymbols(): Call<DataSymbolsModel>

    @GET("latest")
    fun latestRates(@Query("symbols") base: String): Call<DataCurrencyRatesModel>
}