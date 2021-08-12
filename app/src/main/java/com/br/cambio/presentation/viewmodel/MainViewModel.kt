package com.br.cambio.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.cambio.domain.usecase.GetCurrenciesUseCase
import com.br.cambio.presentation.mapper.ExchangePresentation
import com.br.cambio.presentation.model.CurrencyPresentation
import com.br.cambio.utils.Event
import com.br.cambio.utils.SimpleEvent
import com.br.cambio.utils.triggerEvent
import com.br.cambio.utils.triggerPostEvent
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class MainViewModel(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private var currentList: List<CurrencyPresentation> = emptyList()

    private val _emptyResponseEvent = MutableLiveData<SimpleEvent>()
    private val _errorResponseEvent = MutableLiveData<SimpleEvent>()
    private val _showToast = MutableLiveData<SimpleEvent>()
    private val _successResponseEvent = MutableLiveData<Event<List<CurrencyPresentation>>>()

    val errorResponseEvent: LiveData<SimpleEvent>
        get() = _errorResponseEvent
    val successResponseEvent: LiveData<Event<List<CurrencyPresentation>>>
        get() = _successResponseEvent

    fun getCurrency() {
        if (currentList.isNullOrEmpty()) {
            requestList()
        } else {
            _successResponseEvent.triggerEvent(currentList)
        }
    }

    private fun requestList() {

        viewModelScope.launch(dispatcher) {

            runCatching {
                getCurrenciesUseCase()
            }.onSuccess {
                handlerSuccess(it)
            }.onFailure {
                _showToast.triggerEvent()
            }
        }
    }

    private fun handlerSuccess(data: ExchangePresentation) {
        when (data) {
            is ExchangePresentation.EmptyResponse -> _emptyResponseEvent.triggerEvent()
            is ExchangePresentation.ErrorResponse -> _errorResponseEvent.triggerEvent()
            is ExchangePresentation.SuccessResponse -> {
                data.items?.let {
                    currentList = it
                }
                _successResponseEvent.triggerPostEvent(currentList)
            }
        }
    }
}