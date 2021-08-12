package com.br.cambio.customviews

fun Spinner.getList(): DialogSpinnerEnum? {
    return when (spinnerType) {
        Spinner.SpinnerType.CURRENCY -> DialogSpinnerEnum.CURRENTY
        else -> DialogSpinnerEnum.CURRENTY
    }
}