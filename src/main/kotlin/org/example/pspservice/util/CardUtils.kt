package org.example.pspservice.util

object CardUtils {

    fun isValidLuhn(cardNumber: String): Boolean {
        val digits = cardNumber.filter { it.isDigit() }.map { it.toString().toInt() }
        val sum = digits.reversed().mapIndexed { index, digit ->
            if (index % 2 == 1) {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            } else digit
        }.sum()
        return sum % 10 == 0
    }

    fun getBin(cardNumber: String): String = cardNumber.take(6)

    fun binSumEven(bin: String): Boolean {
        val sum = bin.map { it.toString().toInt() }.sum()
        return sum % 2 == 0
    }
}