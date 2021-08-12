package com.br.cambio.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.br.cambio.presentation.model.CurrencyPresentation

internal object RepositoryDiff : DiffUtil.ItemCallback<CurrencyPresentation>() {
    override fun areItemsTheSame(
        oldItem: CurrencyPresentation,
        newItem: CurrencyPresentation
    ): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(
        oldItem: CurrencyPresentation,
        newItem: CurrencyPresentation
    ): Boolean {
        return oldItem == newItem
    }
}