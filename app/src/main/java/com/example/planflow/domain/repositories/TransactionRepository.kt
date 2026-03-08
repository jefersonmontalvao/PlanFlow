package com.example.planflow.domain.repositories

import com.example.planflow.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: String): Transaction?

    suspend fun addTransaction(transaction: Transaction)

    suspend fun delTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)
}