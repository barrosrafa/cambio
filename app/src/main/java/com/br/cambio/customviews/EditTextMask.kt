package com.br.cambio.customviews

import com.br.cambio.customviews.FormField.Companion.TEXT_DEFAULT_INCOME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CEP
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNPJ
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CPF
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_INCOME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_REQUIRED
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE_MM_YYYY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PHONE

const val MASK_CPF = "###.###.###-##"
const val MASK_DATE = "##/##/####"
const val MASK_DATE_MM_YYYY = "##/####"
const val MASK_PHONE = "(##) #####-####"
const val MASK_CEP = "#####-###"
const val MASK_CNPJ = "##.###.###/####-##"

fun FormField.mask(): String {
    return when (fieldType) {
        FIELD_TYPE_CPF -> MASK_CPF
        FIELD_TYPE_CNPJ -> MASK_CNPJ
        FIELD_TYPE_DATE -> MASK_DATE
        FIELD_TYPE_PHONE -> MASK_PHONE
        FIELD_TYPE_CEP -> MASK_CEP
        FIELD_TYPE_DATE_MM_YYYY -> MASK_DATE_MM_YYYY
        else -> ""
    }
}

fun FormField.setupMask() {
    when (fieldType) {
        FIELD_TYPE_CURRENCY_INCOME -> setCurrencyIncomeTextWatcher()
        FIELD_TYPE_CURRENCY, FIELD_TYPE_CURRENCY_REQUIRED  -> setCurrencyTextWatcher()
        else -> setCurrencyTextWatcher()
    }
}

private fun FormField.setCurrencyTextWatcher() {
    editText?.addTextChangedListener(CurrencyTextWatcher(editText))
    setTextWithoutHintAnimation(TEXT_DEFAULT_INCOME)
}

private fun FormField.setCurrencyIncomeTextWatcher() {
    editText?.addTextChangedListener(CurrencyTextWatcher(editText))
}