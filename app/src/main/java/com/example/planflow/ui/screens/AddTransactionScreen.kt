package com.example.planflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign
import java.util.Currency
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.example.planflow.R
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.models.TransactionType
import com.example.planflow.ui.components.bottombar.BottomBar
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.navigation.AppNavigator
import com.example.planflow.ui.viewmodels.TransactionViewModel
import com.example.planflow.utils.currency.toBigDecimalFromCents
import com.example.planflow.utils.currency.toCurrency
import com.example.planflow.utils.date.formatAsShortDate
import com.example.planflow.utils.date.toLocalDate
import java.time.LocalDate


@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel,
    navigator: AppNavigator
) {
    var transactionName by rememberSaveable { mutableStateOf("") }
    var transactionAmount by rememberSaveable { mutableStateOf("") }
    var transactionType by rememberSaveable { mutableStateOf(TransactionType.EXPENSE) }
    var transactionDetails by rememberSaveable { mutableStateOf("") }
    var transactionDate by rememberSaveable { mutableStateOf(LocalDate.now())}

    Scaffold(
        topBar = {
            TopBar(stringResource(R.string.screen_title_add_transaction))
        },
        bottomBar = {
            BottomBar(
                onHomeClick = { navigator.goToHome() },
                onSettingsClick = { navigator.goToSettings() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newTransaction = Transaction(
                        name = transactionName,
                        description = transactionDetails,
                        amount = transactionAmount.toBigDecimalFromCents(),
                        type = transactionType,
                        date = transactionDate
                    )
                    viewModel.addTransaction(newTransaction)

                    navigator.goBack()
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = stringResource(R.string.action_save_transaction))
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
                value = transactionName,
                onValueChange = { nameInput ->
                    if (nameInput.length <= Transaction.MAX_NAME_LENGTH) {
                        transactionName = nameInput
                    }
                                },
                label = { Text(stringResource(R.string.label_transaction_name)) },
                maxLines = 1,
                shape = RoundedCornerShape(5.dp),
                isError = transactionName.length >= Transaction.MAX_NAME_LENGTH,
                modifier = Modifier.fillMaxWidth()
            )

            // Amount field
            val amountAsCurrency = transactionAmount
                .toBigDecimalFromCents()
                .toCurrency()
            val amountFieldValue = remember(transactionAmount) {
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
                        transactionAmount = cents
                    }
                },
                label = { Text(stringResource(R.string.label_transaction_amount)) },
                prefix = { Text(Currency.getInstance(Locale.getDefault()).symbol + " ") }, singleLine = true,
                shape = RoundedCornerShape(5.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                modifier = Modifier.fillMaxWidth()
            )

                TransactionTypeSegmentedMenu(
                    onTypeSelected = { transactionType = it },
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerDocked(
                    defaultDate = transactionDate,
                    changeSelectedDate = { selectedDate ->
                        transactionDate = selectedDate
                    }
                )

            // Details field
            OutlinedTextField(
                value = transactionDetails,
                onValueChange = { descriptionInput ->
                    if (descriptionInput.length <= Transaction.MAX_DESCRIPTION_LENGTH) {
                        transactionDetails = descriptionInput
                    }
                },
                label = { Text(stringResource(R.string.label_transaction_description)) },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = "${transactionDetails.length} / ${Transaction.MAX_DESCRIPTION_LENGTH}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (transactionDetails.length >= Transaction.MAX_DESCRIPTION_LENGTH) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = transactionDetails.length >= Transaction.MAX_DESCRIPTION_LENGTH
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDocked(
    defaultDate: LocalDate,
    changeSelectedDate: (date: LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDateFieldValue = datePickerState.selectedDateMillis?.toLocalDate()?.formatAsShortDate() ?: defaultDate.formatAsShortDate()

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDateFieldValue,
            onValueChange = { },
            label = { Text(stringResource(R.string.label_transaction_date)) },
            readOnly = true,
            shape = RoundedCornerShape(5.dp),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.cd_select_date)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                changeSelectedDate(it.toLocalDate())
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text(stringResource(R.string.action_ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true
                )
            }
        }
    }
}

@Composable
private fun TransactionTypeSegmentedMenu(modifier: Modifier, onTypeSelected: (type: TransactionType) -> Unit) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(
        stringResource(R.string.transaction_type_expense),
        stringResource(R.string.transaction_type_income)
    )

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(5.dp)
                ),
                onClick = {
                    selectedIndex = index
                    val newTransactionType = if (index == 0) TransactionType.EXPENSE else TransactionType.INCOME
                    onTypeSelected(newTransactionType)
                          },
                selected = index == selectedIndex,
                label = { Text(label) },
            )
        }
    }
}
