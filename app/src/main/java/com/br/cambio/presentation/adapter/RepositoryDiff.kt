package com.br.cambio.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.br.cambio.customviews.DialogSpinnerModel

internal object RepositoryDiff : DiffUtil.ItemCallback<DialogSpinnerModel>() {
    override fun areItemsTheSame(
        oldItem: DialogSpinnerModel,
        newItem: DialogSpinnerModel
    ): Boolean {
        return oldItem.codigo == newItem.codigo
    }

    override fun areContentsTheSame(
        oldItem: DialogSpinnerModel,
        newItem: DialogSpinnerModel
    ): Boolean {
        return oldItem == newItem
    }
}