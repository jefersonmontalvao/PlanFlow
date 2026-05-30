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
        require(name.length <= MAX_NAME_LENGTH) {
            "Name cannot exceed $MAX_NAME_LENGTH characters"
        }
        require(amount > BigDecimal.ZERO) {
            "Amount must to be greater than zero"
        }
        require(description.length <= MAX_DESCRIPTION_LENGTH) {
            "Description cannot exceed $MAX_DESCRIPTION_LENGTH characters"
        }
    }

    val signedAmount: BigDecimal
    get() = if (type == TransactionType.EXPENSE) {
            amount.negate()
        } else {
            amount
        }

    companion object {
        const val MAX_NAME_LENGTH = 45
        const val MAX_DESCRIPTION_LENGTH = 150
    }
}
