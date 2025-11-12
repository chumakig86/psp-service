package org.example.pspservice.model

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class PaymentRequest(

    @field:Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    val cardNumber: String, // Keep as String to preserve leading zeros and avoid numeric overflow

    @field:Min(1)
    @field:Max(12)
    val expiryMonth: Int, // Int, because months are numeric and fixed range

    @field:Min(2025)
    val expiryYear: Int, // Int, to validate expiration properly

    @field:Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
    val cvv: String, // String, because leading zeros matter

    @field:DecimalMin(value = "0.01", message = "Amount must be positive")
    val amount: BigDecimal, // BigDecimal to avoid floating-point errors

    @field:Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    val currency: String, // String, ISO 4217

    @field:NotBlank
    val merchantId: String
)