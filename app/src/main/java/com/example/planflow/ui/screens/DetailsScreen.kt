package com.example.planflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planflow.R
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.navigation.AppNavigator
import com.example.planflow.ui.viewmodels.TransactionViewModel
import com.example.planflow.utils.currency.toCurrencyString
import com.example.planflow.utils.date.formatAsShortDate

@Composable
fun DetailsScreen(
    viewModel: TransactionViewModel,
    navigator: AppNavigator,
    transactionId: String?
) {
    val transactions by viewModel.transactions.collectAsState()
    val selectedTransaction = transactions.find { it.id == transactionId }!!

    Scaffold(
        topBar = { TopBar(stringResource(R.string.screen_title_details), showBackIcon = true, onBackClick = { navigator.goBack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column {
                Text(text = stringResource(R.string.label_transaction_name), style = MaterialTheme.typography.labelMedium)
                Text(selectedTransaction.name, style = MaterialTheme.typography.bodyLarge)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = stringResource(R.string.label_transaction_amount), style = MaterialTheme.typography.labelMedium)
                    Text(selectedTransaction.amount.toCurrencyString(), style = MaterialTheme.typography.bodyLarge)
                }

                Column {
                    Text(text = stringResource(R.string.label_transaction_type), style = MaterialTheme.typography.labelMedium)

                    val transactionType = when (selectedTransaction.type) {
                        TransactionType.EXPENSE ->
                            stringResource(R.string.transaction_type_expense)

                        TransactionType.INCOME ->
                            stringResource(R.string.transaction_type_income)
                    }
                    Text(
                        text = transactionType,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Column {
                Text(text = stringResource(R.string.label_transaction_date), style = MaterialTheme.typography.labelMedium)
                Text(selectedTransaction.date.formatAsShortDate(), style = MaterialTheme.typography.bodyLarge)
            }

            Column {
                Text(text = stringResource(R.string.label_transaction_description), style = MaterialTheme.typography.labelMedium)
                Text(selectedTransaction.description, style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
