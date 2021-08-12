package com.br.cambio.customviews

fun String.replaceNotApplicable(): String {
    return if (this.equals("NAO SE APLICA", true)) {
        ""
    } else {
        this
    }
}

fun String.replaceUnknown(): String {
    return if (this.equals("nao informado", true)) {
        ""
    } else {
        this
    }
}