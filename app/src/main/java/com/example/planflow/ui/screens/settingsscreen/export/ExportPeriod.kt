package com.example.planflow.ui.screens.settingsscreen.export

import java.time.LocalDate

sealed interface ExportPeriod {

    data object All : ExportPeriod

    data class Custom(
        val initialDate: LocalDate,
        val finalDate: LocalDate
    ) : ExportPeriod
}
