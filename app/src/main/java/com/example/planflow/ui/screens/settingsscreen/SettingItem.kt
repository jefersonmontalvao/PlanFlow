package com.example.planflow.ui.screens.settingsscreen

sealed interface SettingItem {
    val id: SettingId
    val title: String
    val description: String

    data class Select<T>(
        override val id: SettingId,
        override val title: String,
        override val description: String,
        val state: SelectState<T>
    ): SettingItem

    data class Info(
        override val id: SettingId,
        override val title: String,
        override val description: String,
    ) : SettingItem

    data class Action(
        override val id: SettingId,
        override val title: String,
        override val description: String,
    ) : SettingItem

}

data class SelectState<T>(
    val availableItems: List<SelectableOption<T>>,
    val selectedItem: SelectableOption<T>
)

data class SelectableOption<T>(
    val value: T,
    val label: String
)
