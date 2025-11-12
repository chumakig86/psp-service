package org.example.pspservice.controller

import jakarta.validation.Valid
import org.example.pspservice.model.PaymentRequest
import org.example.pspservice.model.PaymentResponse
import org.example.pspservice.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping
    fun processPayment(@Valid @RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val response = paymentService.processPayment(request)
        return  ResponseEntity.ok(response)
    }
}