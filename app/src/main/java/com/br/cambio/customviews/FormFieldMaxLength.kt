package com.br.cambio.customviews

import android.text.InputFilter
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_ADDRESS
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CEP
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CITY_DISTRICT
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNH
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNPJ
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_COMPANY_NAME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_COMPLEMENT
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CPF
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_INCOME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_REQUIRED
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE_MM_YYYY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_EMAIL
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NAME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PASSWORD
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PHONE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_RG
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_RNE

const val MAX_LENGTH_DEFAULT = 30
const val MAX_LENGTH_CPF = MASK_CPF.length
const val MAX_LENGTH_CNPJ = MASK_CNPJ.length
const val MAX_LENGTH_NAME_WARNING = 31
const val MAX_LENGTH_DATE = MASK_DATE.length
const val MAX_LENGTH_DATE_MM_YYYY = MASK_DATE_MM_YYYY.length
const val MAX_LENGTH_PHONE = MASK_PHONE.length
const val MAX_LENGTH_CURRENCY = 17
const val MAX_LENGTH_PASSWORD = 6
const val MAX_LENGTH_CEP = MASK_CEP.length
const val MAX_LENGTH_CNH = 11
const val MAX_LENGTH_RG_RNE = 20
const val MAX_LENGTH_EMAIL = 80
const val MAX_LENGTH_CITY_DISTRICT_WARNING = 16
const val MAX_LENGTH_COMPLEMENT = 15
const val MAX_LENGTH_ADDRESS_WARNING = 21
const val MAX_LENGTH_COMPANY_NAME = 50



fun FormField.maxLength(): Int {
    return when (fieldType) {
        FIELD_TYPE_CPF -> MAX_LENGTH_CPF
        FIELD_TYPE_CNPJ -> MAX_LENGTH_CNPJ
        FIELD_TYPE_NAME -> MAX_LENGTH_NAME_WARNING
        FIELD_TYPE_CITY_DISTRICT -> MAX_LENGTH_CITY_DISTRICT_WARNING
        FIELD_TYPE_EMAIL -> MAX_LENGTH_EMAIL
        FIELD_TYPE_DATE -> MAX_LENGTH_DATE
        FIELD_TYPE_DATE_MM_YYYY -> MAX_LENGTH_DATE_MM_YYYY
        FIELD_TYPE_PHONE -> MAX_LENGTH_PHONE
        FIELD_TYPE_CURRENCY_INCOME, FIELD_TYPE_CURRENCY, FIELD_TYPE_CURRENCY_REQUIRED -> MAX_LENGTH_CURRENCY
        FIELD_TYPE_PASSWORD -> MAX_LENGTH_PASSWORD
        FIELD_TYPE_CEP -> MAX_LENGTH_CEP
        FIELD_TYPE_CNH -> MAX_LENGTH_CNH
        FIELD_TYPE_RG,
        FIELD_TYPE_RNE -> MAX_LENGTH_RG_RNE
        FIELD_TYPE_COMPLEMENT -> MAX_LENGTH_COMPLEMENT
        FIELD_TYPE_COMPANY_NAME -> MAX_LENGTH_COMPANY_NAME
        FIELD_TYPE_ADDRESS -> MAX_LENGTH_ADDRESS_WARNING
        else -> MAX_LENGTH_DEFAULT
    }
}

fun FormField.setMaxLength(maxLength: Int) {
    if (maxLength == 0) {
        editText?.filters = arrayOf(InputFilter.LengthFilter(maxLength()))
    } else {
        editText?.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }
}