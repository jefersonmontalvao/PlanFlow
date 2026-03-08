package com.example.planflow.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.domain.repositories.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
): ViewModel() {
    val transactions = repository.getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val incomeTransactions = transactions.map { transactions ->
        transactions.filter { transaction ->
            transaction.type == TransactionType.INCOME
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val expenseTransactions = transactions.map { transactions ->
        transactions.filter { transaction ->
            transaction.type == TransactionType.EXPENSE
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun delTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delTransaction(transaction)
        }
    }

    fun updateTransaction(updatedTransaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(updatedTransaction)
        }
    }
}