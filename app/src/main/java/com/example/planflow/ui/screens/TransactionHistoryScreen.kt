package com.example.planflow.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planflow.R
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.ui.components.bottombar.BottomBar
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.components.transaction.TransactionItem
import com.example.planflow.ui.navigation.AppNavigator
import com.example.planflow.ui.viewmodels.TransactionViewModel
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun TransactionsHistoryScreen(
    viewModel: TransactionViewModel,
    navigator: AppNavigator
) {
    val transactions by viewModel.transactions.collectAsState()

    Scaffold (
        topBar = {
            TopBar(stringResource(R.string.screen_title_transactions_history))
        },
        bottomBar = {
            BottomBar(
                atHome = true,
                atSettings = false,
                onHomeClick = { },
                onSettingsClick = { navigator.goToSettings() }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.goToAddTransaction() }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            LazyColumn (
                modifier = Modifier.fillMaxWidth()
            ) {
                items(transactions) { transaction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TransactionItem(
                            transaction = transaction,
                            onTransactionClick = {

                            }

                        )
                    }
                }
            }
        }
    }
}

