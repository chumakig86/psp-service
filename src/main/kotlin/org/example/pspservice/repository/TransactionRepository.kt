package org.example.pspservice.repository

import org.example.pspservice.model.Transaction

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: String): Transaction?
    fun findAll(): List<Transaction>
}