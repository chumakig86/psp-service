package org.example.pspservice.service

import org.example.pspservice.model.PaymentRequest
import org.example.pspservice.model.PaymentResponse

interface PaymentService {
    fun processPayment(request: PaymentRequest): PaymentResponse
}