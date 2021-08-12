package com.br.cambio.customviews

import android.text.method.DigitsKeyListener
import com.br.cambio.R
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_INCOME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_ADDRESS
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CEP
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNH
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNPJ
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CPF
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_REQUIRED
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_EMAIL
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE_MM_YYYY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NAME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NUMBER
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PASSWORD
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PHONE

const val ALLOWED_DIGITS_OTHER = R.string.digits_alphanumeric
const val ALLOWED_DIGITS_NAME = R.string.digits_alpha
const val ALLOWED_DIGITS_CPF = R.string.digits_cpf
const val ALLOWED_DIGITS_CNPJ = R.string.digits_cpf
const val ALLOWED_DIGITS_DATE = R.string.digits_date
const val ALLOWED_DIGITS_EMAIL = R.string.digits_email
const val ALLOWED_DIGITS_PHONE = R.string.digits_phone
const val ALLOWED_DIGITS_CURRENCY = R.string.digits_currency
const val ALLOWED_DIGITS_PASSWORD = R.string.digits_password
const val ALLOWED_DIGITS_CEP = R.string.digits_cep
const val ALLOWED_DIGITS_NUMBER = R.string.digits_number
const val ALLOWED_DIGITS_CNH = R.string.digits_cnh

fun FormField.allowedDigits(): String {
    return when (fieldType) {
        FIELD_TYPE_NAME -> context.getString(ALLOWED_DIGITS_NAME)
        FIELD_TYPE_ADDRESS -> context.getString(ALLOWED_DIGITS_NAME)
        FIELD_TYPE_CPF -> context.getString(ALLOWED_DIGITS_CPF)
        FIELD_TYPE_CNPJ -> context.getString(ALLOWED_DIGITS_CNPJ)
        FIELD_TYPE_DATE, FIELD_TYPE_DATE_MM_YYYY -> context.getString(ALLOWED_DIGITS_DATE)
        FIELD_TYPE_EMAIL -> context.getString(ALLOWED_DIGITS_EMAIL)
        FIELD_TYPE_PHONE -> context.getString(ALLOWED_DIGITS_PHONE)
        FIELD_TYPE_CURRENCY_INCOME, FIELD_TYPE_CURRENCY, FIELD_TYPE_CURRENCY_REQUIRED -> context.getString(ALLOWED_DIGITS_CURRENCY)
        FIELD_TYPE_PASSWORD -> context.getString(ALLOWED_DIGITS_PASSWORD)
        FIELD_TYPE_CEP -> context.getString(ALLOWED_DIGITS_CEP)
        FIELD_TYPE_NUMBER -> context.getString(ALLOWED_DIGITS_NUMBER)
        FIELD_TYPE_CNH -> context.getString(ALLOWED_DIGITS_CNH)
        else -> context.getString(ALLOWED_DIGITS_OTHER)
    }
}

fun FormField.setAllowedDigits() {
    val allowedDigits = allowedDigits()
    if (allowedDigits.isNotEmpty()) {
        editText?.keyListener = DigitsKeyListener.getInstance(allowedDigits)
    }
}