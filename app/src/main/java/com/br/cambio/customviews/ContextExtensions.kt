package com.br.cambio.customviews

import android.content.Context

fun Context.dpToInt(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}