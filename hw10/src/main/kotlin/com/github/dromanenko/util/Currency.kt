package com.github.dromanenko.util

enum class Currency(private val rate: Double) {
    RUB(1.0), USD(83.5), EUR(94.5);

    companion object {
        const val PARAM_CURRENCY = "currency"

        fun convert(from: Currency, to: Currency, amount: Double): Double = amount * from.rate / to.rate

        fun getCurrencyFromString(currencyString: String): Currency = valueOf(currencyString.uppercase())
    }
}