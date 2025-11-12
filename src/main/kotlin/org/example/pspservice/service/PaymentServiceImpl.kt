package org.example.pspservice.service

import org.example.pspservice.model.PaymentRequest
import org.example.pspservice.model.PaymentResponse
import org.example.pspservice.model.Transaction
import org.example.pspservice.repository.TransactionRepository
import org.example.pspservice.util.CardUtils
import org.springframework.stereotype.Service
import java.time.YearMonth
import java.util.*

@Service
class PaymentServiceImpl(
    private val repository: TransactionRepository
) : PaymentService {

    override fun processPayment(request: PaymentRequest): PaymentResponse {

        // 1️⃣ Validate card number (Luhn)
        if (!CardUtils.isValidLuhn(request.cardNumber)) {
            return PaymentResponse("", "Failed", "Invalid card number (Luhn check failed)")
        }

        // 2️⃣ Validate expiry date (must not be in the past)
        val current = YearMonth.now()
        val expiry = YearMonth.of(request.expiryYear, request.expiryMonth)
        if (expiry.isBefore(current)) {
            return PaymentResponse("", "Failed", "Card is expired")
        }

        // 3️⃣ Initialize transaction
        val transactionId = UUID.randomUUID().toString()
        val transaction = Transaction(
            transactionId = transactionId,
            cardNumber = request.cardNumber,
            expiryMonth = request.expiryMonth,
            expiryYear = request.expiryYear,
            cvv = request.cvv,
            amount = request.amount,
            currency = request.currency,
            merchantId = request.merchantId,
            status = "Pending"
        )
        repository.save(transaction)

        // 4️⃣ Routing based on BIN
        val bin = CardUtils.getBin(request.cardNumber)
        val isEven = CardUtils.binSumEven(bin)
        val acquirer = if (isEven) "AcquirerA" else "AcquirerB"

        // 5️⃣ Mock acquirer decision
        val approved = simulateAcquirerDecision(request.cardNumber)
        transaction.status = if (approved) "Approved" else "Denied"
        repository.save(transaction)

        // 6️⃣ Response
        return PaymentResponse(
            transactionId = transaction.transactionId,
            status = transaction.status,
            message = "${transaction.status} by $acquirer"
        )
    }

    private fun simulateAcquirerDecision(cardNumber: String): Boolean {
        val lastDigit = cardNumber.last().digitToIntOrNull() ?: 1
        return lastDigit % 2 == 0 // even last digit = approved
    }
}