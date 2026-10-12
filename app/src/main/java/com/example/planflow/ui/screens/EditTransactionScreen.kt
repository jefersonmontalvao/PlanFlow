package com.example.planflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planflow.R
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.ui.components.bottombar.BottomBar
import com.example.planflow.ui.components.form.DatePickerDocked
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.navigation.AppNavigator
import com.example.planflow.ui.viewmodels.TransactionViewModel
import com.example.planflow.utils.currency.toBigDecimalFromCents
import com.example.planflow.utils.currency.toCurrency
import java.time.LocalDate
import java.util.Currency
import java.util.Locale

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navigator: AppNavigator,
    transactionId: String?
) {
    val transactions by viewModel.transactions.collectAsState()
    val matchedTransaction = transactions.find { it.id == transactionId }

    if (matchedTransaction == null) {
        Scaffold(
            topBar = {
                TopBar(
                    text = stringResource(R.string.screen_title_edit_transaction),
                    showBackIcon = true,
                    onBackClick = { navigator.goBack() }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
        return
    }

    var fieldTransactionName by rememberSaveable { mutableStateOf("") }
    var fieldTransactionAmount by rememberSaveable { mutableStateOf("") }
    var fieldTransactionType by rememberSaveable { mutableStateOf(TransactionType.EXPENSE) }
    var fieldTransactionDetails by rememberSaveable { mutableStateOf("") }
    var fieldTransactionDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    var isInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(matchedTransaction) {
        if (!isInitialized) {
            fieldTransactionName = matchedTransaction.name
            fieldTransactionAmount = matchedTransaction.amount.movePointRight(2).toBigInteger().toString()
            fieldTransactionType = matchedTransaction.type
            fieldTransactionDetails = matchedTransaction.description
            fieldTransactionDate = matchedTransaction.date
            isInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(R.string.screen_title_edit_transaction),
                showBackIcon = true,
                onBackClick = { navigator.goBack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val editedTransaction = Transaction(
                        id = matchedTransaction.id,
                        name = fieldTransactionName,
                        description = fieldTransactionDetails,
                        amount = fieldTransactionAmount.toBigDecimalFromCents(),
                        type = fieldTransactionType,
                        date = fieldTransactionDate
                    )
                    viewModel.updateTransaction(editedTransaction)
                    navigator.goBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.action_save_transaction)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name field.
            OutlinedTextField(
                value = fieldTransactionName,
                onValueChange = { nameInput ->
                    if (nameInput.length <= Transaction.MAX_NAME_LENGTH) {
                        fieldTransactionName = nameInput
                    }
                },
                label = { Text(stringResource(R.string.label_transaction_name)) },
                maxLines = 1,
                shape = RoundedCornerShape(5.dp),
                isError = fieldTransactionName.length >= Transaction.MAX_NAME_LENGTH,
                modifier = Modifier.fillMaxWidth()
            )

            // Amount field
            val amountAsCurrency = fieldTransactionAmount
                .toBigDecimalFromCents()
                .toCurrency()
            val amountFieldValue = remember(fieldTransactionAmount) {
                TextFieldValue(
                    text = amountAsCurrency,
                    selection = TextRange(amountAsCurrency.length)
                )
            }
            OutlinedTextField(
                value = amountFieldValue,
                onValueChange = { amountInput ->
                    val cents = amountInput.text.filter { it.isDigit() }
                    if (cents.length <= 12) {
                        fieldTransactionAmount = cents
                    }
                },
                label = { Text(stringResource(R.string.label_transaction_amount)) },
                prefix = { Text(Currency.getInstance(Locale.getDefault()).symbol + " ") },
                singleLine = true,
                shape = RoundedCornerShape(5.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TransactionTypeSegmentedMenu(
                selectedType = fieldTransactionType,
                onTypeSelected = { fieldTransactionType = it },
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerDocked(
                defaultDate = fieldTransactionDate,
                changeSelectedDate = { selectedDate ->
                    fieldTransactionDate = selectedDate
                }
            )

            // Details field
            OutlinedTextField(
                value = fieldTransactionDetails,
                onValueChange = { descriptionInput ->
                    if (descriptionInput.length <= Transaction.MAX_DESCRIPTION_LENGTH) {
                        fieldTransactionDetails = descriptionInput
                    }
                },
                label = { Text(stringResource(R.string.label_transaction_description_optional)) },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = "${fieldTransactionDetails.length} / ${Transaction.MAX_DESCRIPTION_LENGTH}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (fieldTransactionDetails.length >= Transaction.MAX_DESCRIPTION_LENGTH)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = fieldTransactionDetails.length >= Transaction.MAX_DESCRIPTION_LENGTH
            )
        }
    }
}

@Composable
private fun TransactionTypeSegmentedMenu(
    selectedType: TransactionType,
    onTypeSelected: (type: TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        stringResource(R.string.transaction_type_expense),
        stringResource(R.string.transaction_type_income)
    )
    val selectedIndex = if (selectedType == TransactionType.EXPENSE) 0 else 1

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(5.dp)
                ),
                onClick = {
                    val newTransactionType = if (index == 0) TransactionType.EXPENSE else TransactionType.INCOME
                    onTypeSelected(newTransactionType)
                },
                selected = index == selectedIndex,
                label = { Text(label) },
            )
        }
    }
}
