package com.example.planflow.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planflow.data.repositories.SettingsRepository
import com.example.planflow.domain.models.Theme
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
    private val settingsRepository: SettingsRepository
): ViewModel() {
    init {
        observeTheme()
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

    fun onThemeClicked(item: SettingItem) {
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
}

sealed interface SettingsEvent {
    data object OpenThemeBottomSheet : SettingsEvent

}
