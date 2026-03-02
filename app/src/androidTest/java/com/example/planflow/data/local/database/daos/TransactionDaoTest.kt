package com.example.planflow.data.local.database.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.planflow.data.local.database.AppDatabase
import com.example.planflow.data.local.database.entities.TransactionEntity
import com.example.planflow.domain.models.TransactionType
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: TransactionsDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.transactionsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetTransaction_returnsCorrectData() = runBlocking {
        val transaction = TransactionEntity(
            id = "1",
            name = "Test",
            description = "Desc",
            amount = BigDecimal("249.90"),
            type = TransactionType.INCOME,
            date = LocalDate.of(2026, 1, 10)
        )

        dao.insert(transaction)

        val result = dao.getById("1")

        assertEquals(transaction.id, result?.id)
        assertEquals(transaction.name, result?.name)
        assertEquals(transaction.description, result?.description)
        assertEquals(transaction.amount, result?.amount)
        assertEquals(transaction.type, result?.type)
        assertEquals(transaction.date, result?.date)

    }

    @Test
    fun insertAndDeleteTransaction_returnsNullValue() = runBlocking {
        val transaction = TransactionEntity(
            id = "1",
            name = "Test",
            description = "Desc",
            amount = BigDecimal("249.90"),
            type = TransactionType.INCOME,
            date = LocalDate.of(2026, 1, 10)
        )
        dao.insert(transaction)
        dao.delete(transaction)

        val result = dao.getById("1")

        assertNull(result)
    }

    @Test
    fun getAllTransactions_returnsTransactionList() = runBlocking {
        val transactionsList = listOf(
            TransactionEntity(
            id = "1",
            name = "Bank",
            description = "Desc",
            amount = BigDecimal("249.90"),
            type = TransactionType.EXPENSE,
            date = LocalDate.of(2026, 1, 10)
        ),
            TransactionEntity(
                id = "2",
                name = "School",
                description = "Desc",
                amount = BigDecimal("100.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2026, 2, 26)
            ),
            TransactionEntity(
                id = "3",
                name = "Cash",
                description = "Desc",
                amount = BigDecimal("1050.00"),
                type = TransactionType.EXPENSE,
                date = LocalDate.of(2026, 2, 26)
            )
        )

        for (transaction in transactionsList) {
            dao.insert(transaction)
        }

        val result = dao.getAll().first()

        assertEquals(3, result.size)
    }

    @Test
    fun getAllTransactions_returnsEmptyTransactionList() = runBlocking {
        val result = dao.getAll().first()

        assertEquals(0, result.size)
    }
}