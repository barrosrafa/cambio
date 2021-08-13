package com.br.cambio.customviews

import android.content.Context
import android.view.View

fun Context.dpToInt(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}