package com.example.planflow.ui.components.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.planflow.R

@Composable
fun BottomBar(
    atHome: Boolean = false,
    atSettings: Boolean = false,
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    NavigationBar{
        NavigationBarItem(
            selected = atHome,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text(text = stringResource(R.string.label_nav_home)) }
        )

        NavigationBarItem(
            selected = atSettings,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text(text = stringResource(R.string.label_nav_settings)) }
        )
    }
}