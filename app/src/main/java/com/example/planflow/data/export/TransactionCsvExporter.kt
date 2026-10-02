package com.example.planflow.data.export

import com.example.planflow.domain.models.Transaction

class TransactionCsvExporter {

    fun export(transactions: List<Transaction>): String {
        return buildString {
            appendLine("id,date,type,name,description,amount")

            transactions.forEach { transaction ->
                appendLine(
                    listOf(
                        transaction.id,
                        transaction.date.toString(),
                        transaction.type.toString(),
                        transaction.name,
                        transaction.description,
                        transaction.amount.toPlainString()
                    ).joinToString(",") { escapeCsv(it) }
                )
            }
        }
    }

    private fun escapeCsv(value: String): String {
        val modifiedValue = value.replace("\"", "\"\"")
        return "\"$modifiedValue\""
    }
}