package com.example.planflow.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.planflow.domain.models.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDate
) {

}