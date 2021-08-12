package com.br.cambio.data.datasource

import android.util.Log
import com.br.cambio.data.api.Api
import com.br.cambio.data.mapper.CurrencyToDomainMapper
import com.br.cambio.data.model.Currency
import com.br.cambio.data.model.Result
import com.br.cambio.domain.model.CurrencyDomain
import com.br.cambio.utils.Extensions

class RemoteDataSourceImpl(
    private val service: Api
) : RemoteDataSource {

    private val mapper = CurrencyToDomainMapper()

    override suspend fun getCurrencies(): List<CurrencyDomain>? {
        val response = service.getCurrency()

        return if (response.isSuccessful) {
            checkBody(response.body())
        } else {
            Log.d("erro no request código", response.code().toString())
            null
        }
    }

    private fun checkBody(data: Result?): List<CurrencyDomain> {
        if (Extensions.isNullOrEmpty(data) || data?.currencies.isNullOrEmpty()) {
            return listOf()
        } else {
            val domain: MutableList<CurrencyDomain> = mutableListOf()
            data?.currencies?.forEach { (key, value) ->
                domain.add(
                    mapper.map(
                        Currency(
                            key,
                            value
                        )
                    )
                )
            }

            return domain
        }
    }
}