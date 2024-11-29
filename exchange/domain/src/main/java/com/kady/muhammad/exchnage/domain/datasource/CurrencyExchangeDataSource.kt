package com.kady.muhammad.exchnage.domain.datasource

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import com.kady.muhammad.exchnage.domain.model.DomainCurrencyModel

interface CurrencyExchangeDataSource {
    suspend fun getSymbols(): Result<List<DomainCurrencyModel>, DataError.Network>
    suspend fun getExchangeRate(
        source: String,
        target: String
    ): Result<DomainCurrencyRateModel, DataError.Network>
}