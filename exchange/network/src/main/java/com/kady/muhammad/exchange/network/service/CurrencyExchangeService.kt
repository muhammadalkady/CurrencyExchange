package com.kady.muhammad.exchange.network.service

import com.kady.muhammad.exchange.models.DataSymbolsModel
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyExchangeService {
    @GET("symbols")
    fun getSymbols(): Call<DataSymbolsModel>
}