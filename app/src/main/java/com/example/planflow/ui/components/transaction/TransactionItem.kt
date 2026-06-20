package com.example.planflow.ui.components.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.planflow.R
import com.example.planflow.domain.models.Transaction
import com.example.planflow.utils.date.formatAsShortDate
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun TransactionItem(
    transaction: Transaction,
    onTransactionClick: (transaction: Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = formatTransactionDate(
        todayDate = LocalDate.now(),
        transactionDate = transaction.date,
        todayLabel = stringResource(R.string.label_date_today),
        yesterdayLabel = stringResource(R.string.label_date_yesterday)
    )

    val formattedCurrency = formatCurrency(transaction.signedAmount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTransactionClick(transaction) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                )

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = formattedCurrency,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun formatTransactionDate(todayDate: LocalDate, transactionDate: LocalDate, todayLabel: String, yesterdayLabel: String): String {
    return when (transactionDate) {
        todayDate -> todayLabel
        todayDate.minusDays(1) -> yesterdayLabel
        else -> {
            transactionDate.formatAsShortDate()
        }
    }
}

private fun formatCurrency(amount: BigDecimal): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return currencyFormatter.format(amount)
}