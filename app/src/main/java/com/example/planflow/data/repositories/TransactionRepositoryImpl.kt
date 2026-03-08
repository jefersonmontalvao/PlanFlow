package com.example.planflow.data.repositories

import com.example.planflow.data.local.database.daos.TransactionsDao
import com.example.planflow.data.mappers.toDomain
import com.example.planflow.data.mappers.toEntity
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionsDao
): TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return dao.getAll()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    override suspend fun delTransaction(transaction: Transaction) {
        dao.delete(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.update(transaction.toEntity())
    }
}