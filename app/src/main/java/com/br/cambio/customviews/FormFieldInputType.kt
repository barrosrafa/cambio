package com.br.cambio.customviews

import android.text.InputType
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_INCOME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CEP
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNPJ
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CNH
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CPF
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_CURRENCY_REQUIRED
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_EMAIL
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_DATE_MM_YYYY
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NAME
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NONE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NUMBER
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PASSWORD
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_PHONE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_RG
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_RNE
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_TEXT

fun FormField.inputType(): Int {
    return when (fieldType) {
        FIELD_TYPE_NONE, FIELD_TYPE_TEXT, FIELD_TYPE_RG, FIELD_TYPE_RNE -> {
            InputType.TYPE_CLASS_TEXT
        }
        FIELD_TYPE_CPF, FIELD_TYPE_CNPJ, FIELD_TYPE_NUMBER, FIELD_TYPE_CNH -> {
            InputType.TYPE_CLASS_NUMBER
        }
        FIELD_TYPE_NAME -> {
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME or
                    InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        FIELD_TYPE_DATE -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_VARIATION_NORMAL or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }
        FIELD_TYPE_DATE_MM_YYYY -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_VARIATION_NORMAL or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }
        FIELD_TYPE_EMAIL -> {
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        FIELD_TYPE_PHONE -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_VARIATION_NORMAL
        }
        FIELD_TYPE_CURRENCY_INCOME,FIELD_TYPE_CURRENCY, FIELD_TYPE_CURRENCY_REQUIRED -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        FIELD_TYPE_PASSWORD -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        FIELD_TYPE_CEP -> {
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_VARIATION_NORMAL
        }
        else -> {
            InputType.TYPE_CLASS_TEXT
        }
    }
}

fun FormField.setInputType() {
    editText?.setRawInputType(inputType())
}