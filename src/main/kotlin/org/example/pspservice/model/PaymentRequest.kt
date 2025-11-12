package org.example.pspservice.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.math.BigDecimal

data class PaymentRequest(

    @field:Schema(
        description = "16-digit card number",
        example = "6011111111111117"
    )
    @field:Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    val cardNumber: String, // Keep as String to preserve leading zeros and avoid numeric overflow

    @field:Schema(
        description = "Expiry month of the card (1-12)",
        example = "12"
    )
    @field:Min(1)
    @field:Max(12)
    val expiryMonth: Int, // Int, because months are numeric and fixed range

    @field:Schema(
        description = "Expiry year of the card (>=2025)",
        example = "2027"
    )
    @field:Min(2025)
    val expiryYear: Int, // Int, to validate expiration properly

    @field:Schema(
        description = "3-digit card CVV",
        example = "123"
    )
    @field:Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
    val cvv: String, // String, because leading zeros matter

    @field:Schema(
        description = "Payment amount",
        example = "100.50"
    )
    @field:DecimalMin(value = "0.01", message = "Amount must be positive")
    val amount: BigDecimal, // BigDecimal to avoid floating-point errors

    @field:Schema(
        description = "Currency code in ISO 4217 format",
        example = "USD"
    )
    @field:Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    val currency: String, // String, ISO 4217

    @field:Schema(
        description = "Merchant identifier",
        example = "merchant_123"
    )
    @field:NotBlank
    val merchantId: String
)