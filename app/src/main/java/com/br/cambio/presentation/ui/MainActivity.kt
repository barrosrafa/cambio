package com.br.cambio.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.br.cambio.R
import com.br.cambio.customviews.DialogSpinnerModel
import com.br.cambio.databinding.ActivityMainBinding
import com.br.cambio.presentation.model.PricePresentation
import com.br.cambio.presentation.viewmodel.MainViewModel
import com.br.cambio.utils.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var currencies: List<DialogSpinnerModel>
    private lateinit var prices: List<PricePresentation>
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        subscribeEvents()
        viewModel.getCurrency()
    }

    override fun onResume() {
        super.onResume()

        viewBinding.EditTextMonetaryBrazil.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var typeOne = prices.firstOrNull {
                    it.currency == "USD${viewBinding.spFirst.textFormattedForApi}"
                }

                var typeTwo = prices.firstOrNull {
                    it.currency == "USD${viewBinding.spSecond.textFormattedForApi}"
                }

                typeOne?.let {
                    typeTwo?.let {
                        var number = viewBinding.EditTextMonetaryBrazil.text.toString().toDoubleOrNull()
                        number?.let {
                            var somaOne = typeOne.price * it
                            var somaTwo = typeTwo.price.toFloat() * somaOne.toFloat()

                            var valor = Math.round(somaTwo * 100) / 100.0

                            viewBinding.tvResult.text = "R$ $valor"
                            viewBinding.clResult.visibility = View.VISIBLE
                        }

                    }
                }
            }
        })
    }

    private fun subscribeEvents() {
        viewModel.successCurrencyEvent.subscribe(this) {
            currencies = this.data.sortedBy { it.nome }.toList()
            viewModel.getPrice()
        }

        viewModel.successPriceEvent.subscribe(this) {
            prices = this.data
            setVisibility()
        }

        viewModel.emptyResponseEvent.subscribe(this) {
            viewBinding.loading.hideLoading()
        }

        viewModel.errorResponseEvent.subscribe(this) {
            viewBinding.loading.hideLoading()
        }
    }

    private fun setVisibility() {
        viewBinding.loading.hideLoading()
        viewBinding.EditTextMonetaryBrazil.visibility = View.VISIBLE
        viewBinding.spFirst.setCustomSpinnerList(currencies)
        viewBinding.spFirst.visibility = View.VISIBLE
        viewBinding.spSecond.setCustomSpinnerList(currencies)
        viewBinding.spSecond.visibility = View.VISIBLE
    }
}