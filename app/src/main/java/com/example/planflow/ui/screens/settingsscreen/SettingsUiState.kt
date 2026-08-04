package com.example.planflow.ui.screens.settingsscreen

import com.example.planflow.domain.models.Theme

data class SettingsUiState(
    val theme: Theme = Theme.SYSTEM,
    val notificationsEnabled: Boolean = false,
    val isExporting: Boolean = false,
    val openedItem: SettingItem? = null
)