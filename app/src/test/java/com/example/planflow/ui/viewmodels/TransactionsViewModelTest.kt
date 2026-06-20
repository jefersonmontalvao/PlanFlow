package com.example.planflow.ui.viewmodels

import androidx.compose.runtime.collectAsState
import com.example.planflow.MainDispatcherRule
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.domain.repositories.TransactionRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class TransactionsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var repository: FakeTransactionsRepository
    lateinit var viewModel: TransactionViewModel

    @Before
    fun setup() {
        repository = FakeTransactionsRepository()
        viewModel = TransactionViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addTransaction_shouldAddTransaction() = runTest {
        val transaction = Transaction(
            id = "1",
            name = "test",
            description = "description test",
            amount = BigDecimal("280.00"),
            type = TransactionType.INCOME,
            date = LocalDate.of(2022, 5, 29)
        )

        viewModel.addTransaction(transaction)
        advanceUntilIdle()

        assertTrue(repository.getAllTransactions().first().any {it.id == transaction.id})
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun transactions_shouldReturnAllTransactions() = runTest {
        val job = launch { viewModel.transactions.collect {} }

        val listOfTransactions = listOf<Transaction>(
            Transaction(
                id = "1",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "2",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "3",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            )
        )

        for (transaction in listOfTransactions) {
            repository.addTransaction(transaction)
        }
        advanceUntilIdle()

        val viewModelTransactionsIds = viewModel.transactions.value.map { it.id }.toSet()
        val hasReturnedAllTransactions = listOfTransactions.all { transaction ->
            transaction.id in viewModelTransactionsIds
        }

        assertTrue(hasReturnedAllTransactions)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun incomeTransactions_shouldReturnAllIncomeTransactions() = runTest {
        val job = launch { viewModel.incomeTransactions.collect {} }

        val listOfTransactions = listOf<Transaction>(
            Transaction(
                id = "1",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "2",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "3",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            )
        )

        for (transaction in listOfTransactions) {
            repository.addTransaction(transaction)
        }
        advanceUntilIdle()

        val viewModelIncomeTransactionIds = viewModel.incomeTransactions.value.map { it.id }.toSet()
        val hasReturnedAllIncome = listOfTransactions.filter { it.type == TransactionType.INCOME }
            .all { transaction ->
                transaction.id in viewModelIncomeTransactionIds
            }

        assertTrue(hasReturnedAllIncome)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun expenseTransactions_shouldReturnAllIncomeTransactions() = runTest {
        val job = launch { viewModel.expenseTransactions.collect {} }

        val listOfTransactions = listOf<Transaction>(
            Transaction(
                id = "1",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "2",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "3",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            )
        )

        for (transaction in listOfTransactions) {
            repository.addTransaction(transaction)
        }
        advanceUntilIdle()

        val viewModelTransactionExpenseIds = viewModel.expenseTransactions.value.map { it.id }.toSet()
        val hasReturnedAllExpense = listOfTransactions.filter { transaction -> transaction.type == TransactionType.EXPENSE }
            .all { transaction ->
                transaction.id in viewModelTransactionExpenseIds
            }

        assertTrue(hasReturnedAllExpense)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delTransaction_shouldDeleteTransaction() = runTest {
        val listOfTransactions = listOf<Transaction>(
            Transaction(
                id = "1",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "2",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            ),
            Transaction(
                id = "3",
                name = "test",
                description = "description test",
                amount = BigDecimal("280.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2022, 5, 29)
            )
        )

        for (transaction in listOfTransactions) {
            repository.addTransaction(transaction)
        }
        advanceUntilIdle()

        val transactionToBeDelected = listOfTransactions[2]
        viewModel.delTransaction(transactionToBeDelected)
        advanceUntilIdle()

        val hasTransactionDeleted = repository.getAllTransactions().first().all { transaction ->
            transaction != transactionToBeDelected
        }

        assertTrue(hasTransactionDeleted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateTransaction_shouldUpdateTransaction() = runTest {
        repository.addTransaction(
            Transaction(
                id = "identifier_1",
                name = "test",
                description = "TODO()",
                amount = BigDecimal("250.00"),
                type = TransactionType.INCOME,
                date = LocalDate.of(2021, 12, 11)
            )
        )

        val updatedTransaction =             Transaction(
            id = "identifier_1",
            name = "test",
            description = "TODO()",
            amount = BigDecimal("250.00"),
            type = TransactionType.INCOME,
            date = LocalDate.of(2021, 12, 11)
        )
        viewModel.updateTransaction(updatedTransaction)

        advanceUntilIdle()

        val hasTransactionHasUpdated = repository.getTransactionById(updatedTransaction.id) == updatedTransaction
        assertTrue(hasTransactionHasUpdated)
    }
}

class FakeTransactionsRepository: TransactionRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactions
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactions.value.find { it.id == id }?.copy()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactions.update { current -> current + transaction }
    }

    override suspend fun delTransaction(transaction: Transaction) {
        transactions.update { current ->
            val transactionToBeDelected = transactions.value.find { it.id == transaction.id }!!
            current - transactionToBeDelected
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactions.update { transactionsList ->
            transactionsList.map { transactionItem ->
                if (transactionItem.id == transaction.id) {
                    transaction.copy()
                } else {
                    transactionItem
                }
            }
        }
    }
}