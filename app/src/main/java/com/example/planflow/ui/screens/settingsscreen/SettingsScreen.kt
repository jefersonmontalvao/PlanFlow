package com.example.planflow.ui.screens.settingsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.planflow.R
import com.example.planflow.domain.models.Theme
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.navigation.AppNavigator
import com.example.planflow.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigator: AppNavigator
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState()

    val settingsList: List<SettingItem> = listOf(
        SettingItem.Select(
            id = SettingId.THEME,
            title = stringResource(R.string.setting_item_name_theme),
            description = stringResource(R.string.setting_item_description_theme),
            state = SelectState(
                availableItems = listOf(
                    SelectableOption(
                        value = Theme.SYSTEM,
                        label = Theme.SYSTEM.toText()
                    ),
                    SelectableOption(
                        value = Theme.DARK,
                        label = Theme.DARK.toText()
                    ),
                    SelectableOption(
                        value = Theme.LIGHT,
                        label = Theme.LIGHT.toText()
                    )
                ),
                selectedItem = SelectableOption(
                    value = uiState.theme,
                    label = uiState.theme.toText()
                )
            ),
        ),
        SettingItem.Action(
            id = SettingId.ABOUT,
            title = stringResource(R.string.setting_item_name_about),
            description = stringResource(R.string.setting_item_description_about)
        ),
    )

    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(R.string.screen_title_settings),
                showBackIcon = true,
                onBackClick = { navigator.goBack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(settingsList) { settingItem ->
                SettingItemView(
                    settingItem = settingItem,
                    onClick = { id ->
                        when (id) {
                            SettingId.THEME -> viewModel.onThemeClicked(settingItem)
                            SettingId.ABOUT -> navigator.goToAbout()
                            else -> {}
                        }
                    }
                )
            }
        }
    }

    uiState.openedItem?.let { item ->
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = viewModel::closeBottomSheet
        ) {
            when (item) {
                is SettingItem.Select<*> -> {
                    SelectSettingBottomSheet(
                        settingItem = item,
                        onSelect = when(item.id) {
                            SettingId.THEME -> { value ->
                                viewModel.updateTheme(value as Theme)
                            }

                            else -> null
                        },
                        onDismiss = viewModel::closeBottomSheet
                    )
                }

                else -> {}
            }
        }
    }
}


@Composable
private fun SettingItemView(settingItem: SettingItem, onClick: (SettingId) -> Unit) {
    when(settingItem) {
        is SettingItem.Select<*> -> {
            ListItem(
                headlineContent = { Text(text = settingItem.title) },
                supportingContent = { Text(text = settingItem.description) },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = settingItem.state.selectedItem.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                },
                modifier = Modifier.clickable { onClick(settingItem.id) }
            )
        }

        is SettingItem.Action -> {
            ListItem(
                headlineContent = { Text(text = settingItem.title) },
                supportingContent = { Text(text = settingItem.description) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable { onClick(settingItem.id) }
            )
        }

        is SettingItem.Info -> {
            ListItem(
                headlineContent = { Text(text = settingItem.title) },
                supportingContent = { Text(text = settingItem.description) },
            )
        }

    }
}
