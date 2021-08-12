package com.br.cambio.data.datasource

import com.br.cambio.domain.model.CurrencyDomain

interface RemoteDataSource {
    suspend fun getCurrencies(): List<CurrencyDomain>?
}