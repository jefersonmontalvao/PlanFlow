package com.example.planflow.ui.screens.settingsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsBottomSheet(
    settingItem: SettingItem,
    onDismiss: () -> Unit
) {
    when(settingItem) {
        is SettingItem.Select<*> -> {
            SelectBottomSheet(
                item = settingItem,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun <T> SelectBottomSheet(
    item: SettingItem.Select<T>,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        item.state.availableItems.forEach { option ->
            ListItem(
                headlineContent = { Text(text = option.label) },
                modifier = Modifier.clickable {
                    item.events.onSelectOption(option.value)
                    onDismiss()
                },
                trailingContent = {
                    if (option.value == item.state.actualState.value) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}
