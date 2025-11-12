package org.example.pspservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Payments", description = "Endpoints for managing payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @Operation(summary = "Process a payment", description = "Processes a payment request and returns the result.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Payment processed successfully",
                content = [Content(schema = Schema(implementation = PaymentResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid payment request",
                content = [Content(schema = Schema())]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema())]
            )
        ]
    )
    @PostMapping
    fun processPayment(@Valid @RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val response = paymentService.processPayment(request)
        return  ResponseEntity.ok(response)
    }
}