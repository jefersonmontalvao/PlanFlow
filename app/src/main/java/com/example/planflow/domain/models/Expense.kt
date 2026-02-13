package com.example.planflow.domain.models

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: BigDecimal,
    val date: LocalDate,
) {
    fun editName(newName: String) = this.copy(name = newName)

    fun editValue(newAmount : BigDecimal) = this.copy(amount = newAmount)

    fun editDate(newDate: LocalDate) = this.copy(date = newDate)
}
