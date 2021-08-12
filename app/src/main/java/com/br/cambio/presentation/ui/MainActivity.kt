package com.br.cambio.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.br.cambio.R
import com.br.cambio.customviews.DialogSpinnerModel
import com.br.cambio.databinding.ActivityCurrencyBinding
import com.br.cambio.databinding.ActivityMainBinding
import com.br.cambio.presentation.adapter.CurrencyAdapter
import com.br.cambio.presentation.viewmodel.CurrencyViewModel
import com.br.cambio.presentation.viewmodel.MainViewModel
import com.br.cambio.utils.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModel()
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        subscribeEvents()
        viewModel.getCurrency()
    }

    private fun subscribeEvents() {
        viewModel.successResponseEvent.subscribe(this) {
            var list: MutableList<DialogSpinnerModel> = mutableListOf()

            this.data.sortedBy { it.name }.forEach { result ->
                val item = DialogSpinnerModel(result.symbol, result.name)
                list.add(item)
            }

            viewBinding.spFirst.setCustomSpinnerList(list)
            viewBinding.spSecond.setCustomSpinnerList(list)
        }
    }
}