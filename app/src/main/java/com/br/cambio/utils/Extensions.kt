package com.br.cambio.utils

import com.br.cambio.data.model.Result

object Extensions{
    fun isNullOrEmpty(data: Result?): Boolean {
        return data == null || data.currencies.isNullOrEmpty()
    }

    fun ordinalOf(i: Int) = "$i" + if (i % 100 in 11..13) "th" else when (i % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}