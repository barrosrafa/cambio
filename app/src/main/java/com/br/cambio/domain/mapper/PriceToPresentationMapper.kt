package com.br.cambio.domain.mapper

import com.br.cambio.domain.model.PriceDomain
import com.br.cambio.presentation.model.PricePresentation
import com.br.cambio.utils.Mapper

class PriceToPresentationMapper : Mapper<PriceDomain?, PricePresentation> {

    override fun map(source: PriceDomain?): PricePresentation {
        return PricePresentation(
            currency = source?.currency.orEmpty(),
            price = source?.price?:0.0
        )
    }
}