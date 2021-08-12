package com.br.cambio.customviews

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/***
 * Realiza conversao de dp para pixel
 *
 * @param context utilizado para obter displayMetrics
 */
fun Int.dpToPx(context: Context?): Int {
    context?.let {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)
        return px.toInt()
    }
    return this
}
