package org.example.pspservice.repository

import org.example.pspservice.model.Transaction
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryTransactionRepository : TransactionRepository {
    private val storage = ConcurrentHashMap<String, Transaction>()

    override fun save(transaction: Transaction): Transaction {
        storage[transaction.transactionId] = transaction
        return transaction
    }

    override fun findById(id: String): Transaction? = storage[id]

    override fun findAll(): List<Transaction> = storage.values.toList()
}