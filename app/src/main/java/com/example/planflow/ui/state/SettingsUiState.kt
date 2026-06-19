package com.example.planflow.ui.state

import com.example.planflow.domain.models.Theme

data class SettingsUiState(
    val theme: Theme = Theme.SYSTEM
)