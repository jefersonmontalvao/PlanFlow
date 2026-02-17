package com.example.planflow.data.mappers

import com.example.planflow.data.local.database.entities.TransactionEntity
import com.example.planflow.domain.models.Transaction

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
        id = id,
        name = name,
        description = description,
        amount = amount,
        type = type,
        date = date
    )

fun TransactionEntity.toDomain(): Transaction = Transaction(
        id = id,
        name = name,
        description = description,
        amount = amount,
        type = type,
        date = date
    )