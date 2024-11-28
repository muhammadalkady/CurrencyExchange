package com.kady.muhammad.exchange.data.remote

import com.kady.muhammad.core.domain.error.DataError
import com.kady.muhammad.core.domain.result.Result
import com.kady.muhammad.core.domain.result.map
import com.kady.muhammad.exchange.models.toDomainModel
import com.kady.muhammad.exchange.network.safeCall
import com.kady.muhammad.exchange.network.service.CurrencyExchangeService
import com.kady.muhammad.exchnage.domain.datasource.CurrencyExchangeDataSource
import com.kady.muhammad.exchnage.domain.model.DomainCurrencyModel

class CurrencyExchangeRemoteDataSource(private val service: CurrencyExchangeService) :
    CurrencyExchangeDataSource {
    override suspend fun getSymbols(): Result<List<DomainCurrencyModel>, DataError.Network> {
        return safeCall { service.getSymbols() }.map { it.toDomainModel() }
    }
}