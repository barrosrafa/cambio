package com.br.cambio.customviews

import android.view.View
import android.view.ViewGroup
import com.br.cambio.R

const val FORM_ITEM_SPACING_DEFAULT = R.dimen.margin_formitem_top

fun View.setupItemSpacing() {
    val newTopMargin = resources.getDimension(FORM_ITEM_SPACING_DEFAULT).toInt()

    (layoutParams as? ViewGroup.MarginLayoutParams)?.run {
        if (topMargin == 0) {
            topMargin = newTopMargin
        }
    }
}
