package com.br.cambio.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.br.cambio.R
import com.br.cambio.databinding.ActivityCurrencyBinding
import com.br.cambio.presentation.adapter.CurrencyAdapter
import com.br.cambio.presentation.viewmodel.CurrencyViewModel
import com.br.cambio.utils.subscribe
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyActivity : AppCompatActivity(R.layout.activity_currency) {

    private val viewModel: CurrencyViewModel by viewModel()
    private val viewBinding by lazy {
        ActivityCurrencyBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        CurrencyAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        subscribeEvents()
        configureRecyclerView()
        viewModel.getCurrency()
    }

    private fun subscribeEvents() {
        viewModel.loadingEvent.subscribe(this) {
            viewBinding.loading.isVisible = true
        }

        viewModel.scrollLoadingEvent.subscribe(this) {
            viewBinding.loadingNextPage.isVisible = true
        }

        viewModel.emptyResponseEvent.subscribe(this) {
            showFullResultsSnackbar()
            hideLoading()
        }

        viewModel.fullResultResponseEvent.subscribe(this) {
            showFullResultsSnackbar()
        }

        viewModel.errorResponseEvent.subscribe(this) {
            showGenericErrorSnackbar()
            hideLoading()
        }

        viewModel.successResponseEvent.subscribe(this) {
            viewBinding.appTitle.isVisible = true
            viewBinding.background.isVisible = true
            adapter.update(this.data)
            hideLoading()
        }
    }

    private fun hideLoading() {
        viewBinding.loading.isVisible = false
        viewBinding.loadingNextPage.isVisible = false
    }

    private fun configureRecyclerView() {
        viewBinding.recyclerViewRepositories.adapter = adapter

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        viewBinding.recyclerViewRepositories.layoutManager = layoutManager
    }

    private fun showFullResultsSnackbar() {
        var snackbar: Snackbar? = null
        snackbar = Snackbar.make(
            viewBinding.recyclerViewRepositories,
            R.string.full_results,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.back_to_top) {
                viewBinding.recyclerViewRepositories.scrollToPosition(0)
                snackbar?.dismiss()
            }
        snackbar.show()
    }

    private fun showGenericErrorSnackbar() {
        var snackbar: Snackbar? = null
        snackbar = Snackbar.make(
            viewBinding.recyclerViewRepositories,
            R.string.couldnt_load,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.try_again) {
                viewModel.getCurrency()
                snackbar?.dismiss()
            }
        snackbar.show()
    }
}