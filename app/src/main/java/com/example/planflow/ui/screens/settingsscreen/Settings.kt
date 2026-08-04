package com.example.planflow.ui.screens.settingsscreen

sealed interface SettingItem {
    val title: String
    val description: String

    data class Select<T>(
        override val title: String,
        override val description: String,
        val state: SelectState<T>,
        val events: SelectEvents<T>
    ): SettingItem
}

data class SelectState<T>(
    val availableItems: List<SelectableOption<T>>,
    val actualState: SelectableOption<T>
)

data class SelectEvents<T>(
    val onClick: (SettingItem) -> Unit,
    val onSelectOption: (T) -> Unit
)

data class SelectableOption<T>(
    val value: T,
    val label: String
)
