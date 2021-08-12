package com.br.cambio.presentation.mapper

import com.br.cambio.presentation.model.CurrencyPresentation

sealed class ExchangePresentation {
    object ErrorResponse : ExchangePresentation()
    object EmptyResponse : ExchangePresentation()
    data class SuccessResponse(val items: List<CurrencyPresentation>?) : ExchangePresentation()
}