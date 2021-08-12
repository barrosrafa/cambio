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

private const val INITIAL_PAGE = 1
private const val ZERO = 0

internal class CurrencyViewModel(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private var currentList: List<CurrencyPresentation> = emptyList()

    private val _emptyResponseEvent = MutableLiveData<SimpleEvent>()
    private val _fullResultResponseEvent = MutableLiveData<SimpleEvent>()
    private val _errorResponseEvent = MutableLiveData<SimpleEvent>()
    private val _showLoadingEvent = MutableLiveData<SimpleEvent>()
    private val _showScrollLoadingEvent = MutableLiveData<SimpleEvent>()
    private val _showToast = MutableLiveData<SimpleEvent>()
    private val _successResponseEvent = MutableLiveData<Event<List<CurrencyPresentation>>>()

    val emptyResponseEvent: LiveData<SimpleEvent>
        get() = _emptyResponseEvent
    val fullResultResponseEvent: LiveData<SimpleEvent>
        get() = _fullResultResponseEvent
    val errorResponseEvent: LiveData<SimpleEvent>
        get() = _errorResponseEvent
    val successResponseEvent: LiveData<Event<List<CurrencyPresentation>>>
        get() = _successResponseEvent
    val loadingEvent: LiveData<SimpleEvent>
        get() = _showLoadingEvent
    val scrollLoadingEvent: LiveData<SimpleEvent>
        get() = _showScrollLoadingEvent
    val showToast: LiveData<SimpleEvent>
        get() = _showToast

    fun getCurrency(isScrolling: Boolean = false) {
        if (currentList.isNullOrEmpty() || isScrolling) {
            requestList(isScrolling)
        } else {
            _successResponseEvent.triggerEvent(currentList)
        }
    }

    private fun requestList(isScrolling: Boolean) {

        viewModelScope.launch(dispatcher) {
            handlerLoading(isScrolling)

            runCatching {
                getCurrenciesUseCase()
            }.onSuccess {
                handlerSuccess(it)
            }.onFailure {
                if (isScrolling) {
                    _showToast.triggerEvent()
                } else {
                    _errorResponseEvent.triggerEvent()
                }
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

    private fun handlerLoading(isScrolling: Boolean) {
        if (isScrolling) {
            _showScrollLoadingEvent.triggerEvent()
        } else {
            _showLoadingEvent.triggerEvent()
        }
    }
}