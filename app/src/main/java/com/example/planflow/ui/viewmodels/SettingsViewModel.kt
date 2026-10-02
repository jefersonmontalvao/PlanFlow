package com.example.planflow.ui.viewmodels

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planflow.data.export.TransactionCsvExporter
import com.example.planflow.data.repositories.SettingsRepository
import com.example.planflow.domain.models.Theme
import com.example.planflow.domain.models.Transaction
import com.example.planflow.domain.repositories.TransactionRepository
import com.example.planflow.notification.PlanFlowNotificationManager
import com.example.planflow.ui.screens.settingsscreen.SettingItem
import com.example.planflow.ui.screens.settingsscreen.SettingsUiState
import com.example.planflow.ui.screens.settingsscreen.export.ExportPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val transactionsRepository: TransactionRepository,
): ViewModel() {
    init {
        observeTheme()
        observeIsNotificationsEnabled()
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    private val _exportCsvEvent = MutableSharedFlow<String>()

    val uiState = _uiState.asStateFlow()
    val exportCsvEvent = _exportCsvEvent.asSharedFlow()

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
        }
    }

    fun onNotificationsClicked(item: SettingItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(openedItem = item) }
        }
    }

    fun onExportClicked(item: SettingItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(openedItem = item) }
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

    fun exportDataAsCsv(exportPeriod: ExportPeriod) {
        val csvExporter = TransactionCsvExporter()

        viewModelScope.launch {
            val allTransactions = transactionsRepository
                .getAllTransactions()
                .first()

            val csv = when(exportPeriod)  {
                is ExportPeriod.All -> {
                    csvExporter.export(allTransactions)
                }

                is ExportPeriod.Custom -> {
                    csvExporter.export(
                        allTransactions.filter { transaction ->
                            transaction.date >= exportPeriod.initialDate &&
                            transaction.date <= exportPeriod.finalDate
                        }
                    )
                }
            }

            _exportCsvEvent.emit(csv)
        }

    }

}
