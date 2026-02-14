package com.example.planflow.domain.models

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDate,
) {
    init {
        require(amount > BigDecimal.ZERO)
    }

    val signedAmount: BigDecimal
    get() = if (type == TransactionType.EXPENSE) {
            amount.negate()
        } else {
            amount
        }
}
