package com.example.planflow.ui.screens.settingsscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planflow.R
import com.example.planflow.ui.components.form.DatePickerDocked
import com.example.planflow.ui.screens.settingsscreen.export.ExportPeriod
import java.time.LocalDate

@Composable
fun ExportActionSettingBottomSheet(
    settingItem: SettingItem.CustomAction,
    exportAction: (exportPeriod: ExportPeriod) -> Unit,
    onDismiss: () -> Unit
) {
    var exportPeriod by remember { mutableStateOf<ExportPeriod>(ExportPeriod.All) }
    var firstDateFormValue by remember { mutableStateOf(LocalDate.now()) }
    var secondDateFormValue by remember { mutableStateOf(LocalDate.now()) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.export_data_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.export_data_period),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExportPeriodOption(
            text = stringResource(R.string.export_data_all_period),
            selected = exportPeriod == ExportPeriod.All,
            onClick = { exportPeriod = ExportPeriod.All }
        )

        ExportPeriodOption(
            text = stringResource(R.string.export_data_custom_period),
            selected = exportPeriod is ExportPeriod.Custom,
            onClick = {
                exportPeriod = ExportPeriod.Custom(
                    initialDate = firstDateFormValue,
                    finalDate = secondDateFormValue)
            }
        )

        AnimatedVisibility(visible = exportPeriod is ExportPeriod.Custom) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                DatePickerDocked(
                    defaultDate = firstDateFormValue,
                    changeSelectedDate = { firstDateFormValue = it}
                )
                Spacer(modifier = Modifier.height(12.dp))
                DatePickerDocked(
                    defaultDate = secondDateFormValue,
                    changeSelectedDate = { secondDateFormValue = it}
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (exportPeriod is ExportPeriod.Custom) {
                    if (firstDateFormValue.isBefore(secondDateFormValue)) {
                        exportAction(
                            ExportPeriod.Custom(
                                firstDateFormValue,
                                secondDateFormValue
                            )
                        )
                    } else {
                        exportAction(
                            ExportPeriod.Custom(
                                secondDateFormValue,
                                firstDateFormValue)
                        )
                    }

                } else {
                    exportAction(ExportPeriod.All)
                }
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.export_data_button),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ExportPeriodOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}