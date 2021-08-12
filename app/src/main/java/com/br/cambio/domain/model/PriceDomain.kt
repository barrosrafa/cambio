package com.br.cambio.domain.model

import com.google.gson.annotations.SerializedName

data class PriceDomain(
    val currency: String,
    val price: Double
)