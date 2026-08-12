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
fun <T> SelectSettingBottomSheet(
    settingItem: SettingItem.Select<T>,
    onSelect: ((T) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = settingItem.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        settingItem.state.availableItems.forEach { option ->
            ListItem(
                headlineContent = { Text(text = option.label) },
                modifier = Modifier.clickable {
                    onSelect?.invoke(option.value)
                    onDismiss()
                },
                trailingContent = {
                    if (option.value == settingItem.state.selectedItem.value) {
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
