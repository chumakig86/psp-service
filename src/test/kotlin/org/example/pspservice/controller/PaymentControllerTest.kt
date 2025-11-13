package org.example.pspservice.controller

import org.example.pspservice.service.PaymentService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PaymentController::class)
@Import(PaymentControllerTest.TestConfig::class)
class PaymentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var paymentService: PaymentService

    private val baseUrl = "/api/payments"

    @TestConfiguration
    class TestConfig {
        @Bean
        fun paymentService(): PaymentService = Mockito.mock(PaymentService::class.java)
    }

    @Test
    fun `should reject when card number is not 16 digits`() {
        val invalidJson = """
            {
                "cardNumber": "12345",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 100.00,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when expiry month is out of range`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 13,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 100.00,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when expiry year is before 2025`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2023,
                "cvv": "123",
                "amount": 100.00,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when CVV is not 3 digits`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "12",
                "amount": 100.00,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when amount is zero or negative`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 0.00,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when currency code is not 3 letters`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 100.00,
                "currency": "US",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject when merchantId is blank`() {
        val invalidJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 100.00,
                "currency": "USD",
                "merchantId": ""
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should accept valid request`() {
        val validJson = """
            {
                "cardNumber": "6011111111111117",
                "expiryMonth": 12,
                "expiryYear": 2026,
                "cvv": "123",
                "amount": 150.50,
                "currency": "USD",
                "merchantId": "merchant_123"
            }
        """.trimIndent()

        mockMvc.perform(
            post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson)
        ).andExpect(status().isOk)
    }
}