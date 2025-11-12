package org.example.pspservice.service

import org.example.pspservice.exception.InvalidCardException
import org.example.pspservice.model.PaymentRequest
import org.example.pspservice.model.Transaction
import org.example.pspservice.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import java.math.BigDecimal
import java.time.YearMonth

class PaymentServiceImplTest {

    private lateinit var repository: TransactionRepository
    private lateinit var service: PaymentServiceImpl

    @BeforeEach
    fun setUp() {
        repository = mock(TransactionRepository::class.java)
        service = PaymentServiceImpl(repository)
    }

    @Test
    fun `processPayment should throw InvalidCardException for invalid card`() {
        val request = validRequest(cardNumber = "1234567890123456") // invalid Luhn
        val exception = assertThrows(InvalidCardException::class.java) {
            service.processPayment(request)
        }
        assertEquals("Invalid card number (Luhn check failed)", exception.message)
        verifyNoInteractions(repository) // should not save anything
    }

    @Test
    fun `processPayment should throw InvalidCardException for expired card`() {
        val now = YearMonth.now()
        val request = PaymentRequest(
            cardNumber = "4532015112830366",
            expiryMonth = now.monthValue,
            expiryYear = now.year - 1, // expired
            cvv = "123",
            amount = BigDecimal(50.0),
            currency = "USD",
            merchantId = "merchant-1"
        )
        val exception = assertThrows(InvalidCardException::class.java) {
            service.processPayment(request)
        }
        assertEquals("Card is expired", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `processPayment should approve transaction for even last digit`() {
        val request = validRequest(cardNumber = "4532015112830366") // ends with 6 = approved
        val response = service.processPayment(request)

        val captor = argumentCaptor<Transaction>()
        verify(repository, times(2)).save(captor.capture())
        val savedTransaction = captor.allValues.last()

        assertEquals("Approved", savedTransaction.status)
        assertTrue(response.message!!.contains("AcquirerA") || response.message!!.contains("AcquirerB"))
        assertEquals(savedTransaction.transactionId, response.transactionId)
    }

    @Test
    fun `processPayment should deny transaction for odd last digit`() {
        val request = validRequest(cardNumber = "6011111111111117") // ends with 7 = denied
        val response = service.processPayment(request)

        val captor = argumentCaptor<Transaction>()
        verify(repository, times(2)).save(captor.capture())
        val savedTransaction = captor.allValues.last()

        assertEquals("Denied", savedTransaction.status)
        assertEquals(savedTransaction.transactionId, response.transactionId)
    }

    // Helper to create a valid request
    private fun validRequest(cardNumber: String = "4532015112830366"): PaymentRequest {
        val now = YearMonth.now()
        return PaymentRequest(
            cardNumber = cardNumber,
            expiryMonth = now.monthValue,
            expiryYear = now.year + 1,
            cvv = "123",
            amount = BigDecimal("100.00"),
            currency = "USD",
            merchantId = "merchant-1"
        )
    }
}