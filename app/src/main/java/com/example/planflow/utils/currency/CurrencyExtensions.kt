package com.example.planflow.utils.currency

import android.icu.text.NumberFormat
import java.math.BigDecimal
import java.util.Locale

fun BigDecimal.toCurrencyString(): String {
    return NumberFormat
        .getCurrencyInstance(Locale.getDefault())
        .format(this)
}

fun BigDecimal.toCurrency(): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}

fun String.toBigDecimalFromCents(): BigDecimal {
    val cents = this.toBigDecimalOrNull() ?: BigDecimal.ZERO
    return cents.divide(BigDecimal(100))
}