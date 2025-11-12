package org.example.pspservice.model

import java.math.BigDecimal

data class Transaction(
    val transactionId: String,
    val cardNumber: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvv: String,
    val amount: BigDecimal,
    val currency: String,
    val merchantId: String,
    var status: String
)
