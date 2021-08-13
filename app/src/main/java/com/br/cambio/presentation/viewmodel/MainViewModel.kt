package com.br.cambio.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.cambio.customviews.DialogSpinnerModel
import com.br.cambio.domain.usecase.GetCurrenciesUseCase
import com.br.cambio.domain.usecase.GetPricesUseCase
import com.br.cambio.presentation.mapper.ExchangePresentation
import com.br.cambio.presentation.mapper.QuotaPresentation
import com.br.cambio.presentation.model.PricePresentation
import com.br.cambio.utils.Event
import com.br.cambio.utils.SimpleEvent
import com.br.cambio.utils.triggerEvent
import com.br.cambio.utils.triggerPostEvent
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class MainViewModel(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getPricesUseCase: GetPricesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private var currentList: List<DialogSpinnerModel> = emptyList()
    private var priceList: List<PricePresentation> = emptyList()

    private val _emptyResponseEvent = MutableLiveData<SimpleEvent>()
    private val _errorResponseEvent = MutableLiveData<SimpleEvent>()
    private val _showToast = MutableLiveData<SimpleEvent>()
    private val _successCurrencyEvent = MutableLiveData<Event<List<DialogSpinnerModel>>>()
    private val _successPriceEvent = MutableLiveData<Event<List<PricePresentation>>>()

    val emptyResponseEvent: LiveData<SimpleEvent>
        get() = _emptyResponseEvent
    val errorResponseEvent: LiveData<SimpleEvent>
        get() = _errorResponseEvent
    val successCurrencyEvent: LiveData<Event<List<DialogSpinnerModel>>>
        get() = _successCurrencyEvent
    val successPriceEvent: LiveData<Event<List<PricePresentation>>>
        get() = _successPriceEvent

    fun getCurrency() {
        if (currentList.isNullOrEmpty()) {
            requestCurrency()
        } else {
            _successCurrencyEvent.triggerEvent(currentList)
        }
    }

    private fun requestCurrency() {

        viewModelScope.launch(dispatcher) {

            runCatching {
                getCurrenciesUseCase()
            }.onSuccess {
                handlerCurrencySuccess(it)
            }.onFailure {
                _showToast.triggerEvent()
            }
        }
    }

    private fun handlerCurrencySuccess(data: ExchangePresentation) {
        when (data) {
            is ExchangePresentation.EmptyResponse -> _emptyResponseEvent.triggerEvent()
            is ExchangePresentation.ErrorResponse -> _errorResponseEvent.triggerEvent()
            is ExchangePresentation.SuccessResponse -> {
                data.items?.let {
                    currentList = it
                }
                _successCurrencyEvent.triggerPostEvent(currentList)
            }
        }
    }

    fun getPrice() {
        if (priceList.isNullOrEmpty()) {
            requestPrice()
        } else {
            _successPriceEvent.triggerEvent(priceList)
        }
    }

    private fun requestPrice() {

        viewModelScope.launch(dispatcher) {

            runCatching {
                getPricesUseCase()
            }.onSuccess {
                handlerPriceSuccess(it)
            }.onFailure {
                _showToast.triggerEvent()
            }
        }
    }

    private fun handlerPriceSuccess(data: QuotaPresentation) {
        when (data) {
            is QuotaPresentation.EmptyResponse -> _emptyResponseEvent.triggerEvent()
            is QuotaPresentation.ErrorResponse -> _errorResponseEvent.triggerEvent()
            is QuotaPresentation.SuccessResponse -> {
                data.items?.let {
                    priceList = it
                }
                _successPriceEvent.triggerPostEvent(priceList)
            }
        }
    }
}