package com.example.planflow.ui.screens.settingsscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.planflow.R
import com.example.planflow.domain.models.Theme

@Composable
fun Theme.toText(): String {
    return when(this) {
        Theme.SYSTEM -> stringResource(R.string.setting_theme_system)
        Theme.DARK -> stringResource(R.string.setting_theme_dark)
        Theme.LIGHT -> stringResource(R.string.setting_theme_light)
    }
}