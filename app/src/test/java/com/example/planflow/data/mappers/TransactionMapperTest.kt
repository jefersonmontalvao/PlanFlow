package com.example.planflow.data.mappers

import com.example.planflow.data.local.database.entities.TransactionEntity
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class TransactionMapperTest {

    @Test
    fun `to domain should map entity to do domain correctly`() {
        val entity = TransactionEntity(
            id = "1",
            name = "Test",
            description = "A income test",
            amount = BigDecimal("100.0"),
            date = LocalDate.of(2024, 1, 9),
            type = TransactionType.INCOME
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.description, domain.description)
        assertEquals(entity.amount, domain.amount)
        assertEquals(entity.date, domain.date)
        assertEquals(entity.type, domain.type)
    }

    @Test
    fun `to entity should map domain to do entity correctly`() {
        val domain = Transaction(
            id = "1",
            name = "Test",
            description = "A income test",
            amount = BigDecimal("100.0"),
            date = LocalDate.of(2026, 1, 9),
            type = TransactionType.INCOME
        )

        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.description, entity.description)
        assertEquals(domain.amount, entity.amount)
        assertEquals(domain.date, entity.date)
        assertEquals(domain.type, entity.type)
    }
}