package org.example.pspservice.model

data class PaymentResponse(
    val transactionId: String,
    val status: String,
    val message: String
)
