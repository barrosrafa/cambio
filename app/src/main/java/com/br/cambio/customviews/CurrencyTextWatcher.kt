package com.br.cambio.customviews

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.text.DecimalFormat

class CurrencyTextWatcher(private val editText: EditText?) : TextWatcher {

    private val format: DecimalFormat? = null

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
        /* Não implementado */
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        /* Não implementado */
    }

    override fun afterTextChanged(s: Editable) {
        editText?.removeTextChangedListener(this)
        /**
         * Foi retirado importância para acessibilidade devido a um erro que era falado uma vírgula pelo leitor
         * de tela.
         */
        editText?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        var index = s.toString().length - 1
        while (index >= 0) {
            val c = s.toString()[index]
            if (!c.isDigit()) s.delete(index, index + 1)
            index--
        }

        while (s.length > 3 && s[0] == '0') s.delete(0, 1)

        while (s.length < 3) s.insert(0, "0")

        s.insert(s.length - 2, ",")

        index = s.toString().length - 6
        while (index > 0 && s.toString().length >= 6) {
            s.insert(index, ".")
            index -= 3
        }
        s.insert(0, "R$ ")

        editText?.addTextChangedListener(this)
        editText?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        editText?.layoutParams = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    fun getCurrencySymbol(): String? {
        return format?.decimalFormatSymbols?.currencySymbol
    }
}