package com.example.planflow.ui.viewmodels

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planflow.data.repositories.SettingsRepository
import com.example.planflow.domain.models.Theme
import com.example.planflow.notification.PlanFlowNotificationManager
import com.example.planflow.ui.screens.settingsscreen.SettingItem
import com.example.planflow.ui.screens.settingsscreen.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val planFlowNotificationManager: PlanFlowNotificationManager
): ViewModel() {
    init {
        observeTheme()
        observeIsNotificationsEnabled()
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    private val _events = MutableSharedFlow<SettingsEvent>()

    val uiState = _uiState.asStateFlow()
    val events = _events.asSharedFlow()

    private fun observeTheme() {
        viewModelScope.launch {
            settingsRepository.themeFlow.collect { theme ->
                _uiState.update { it.copy(theme = theme) }
            }
        }
    }

    private fun observeIsNotificationsEnabled() {
        viewModelScope.launch {
            settingsRepository.isNotificationsEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            }
        }
    }

    fun onThemeClicked(item: SettingItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(openedItem = item) }
            _events.emit(SettingsEvent.OpenThemeBottomSheet)
        }
    }

    fun onNotificationsClicked(item: SettingItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(openedItem = item) }
            _events.emit(SettingsEvent.OpenThemeBottomSheet)
        }
    }

    fun closeBottomSheet() {
        _uiState.update { it.copy(openedItem = null) }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun toggleNotifications() {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(!uiState.value.notificationsEnabled)
        }
    }
}

sealed interface SettingsEvent {
    data object OpenThemeBottomSheet : SettingsEvent

}
