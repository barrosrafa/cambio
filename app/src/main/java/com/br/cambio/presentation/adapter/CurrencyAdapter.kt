package com.br.cambio.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.cambio.customviews.DialogSpinnerModel
import com.br.cambio.databinding.RecyclerCurrencyItemBinding

internal class CurrencyAdapter(
) : ListAdapter<DialogSpinnerModel, RecyclerView.ViewHolder>(RepositoryDiff) {

    private var repositories: List<DialogSpinnerModel> = emptyList()
    private lateinit var viewBinding: RecyclerCurrencyItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        viewBinding = RecyclerCurrencyItemBinding.inflate(inflater)

        return ItemsViewHolder(
            RecyclerCurrencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context
        )
    }

    fun update(repos: List<DialogSpinnerModel>) {
        repositories = emptyList()
        repositories = repos
        submitList(repositories)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ItemsViewHolder -> {
                holder.update(repositories[position])
            }
        }
    }

    inner class ItemsViewHolder(
        private val binding: RecyclerCurrencyItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun update(repositoryItem: DialogSpinnerModel) {
            binding.initials.text = repositoryItem.codigo
            binding.name.text = repositoryItem.nome
        }
    }
}