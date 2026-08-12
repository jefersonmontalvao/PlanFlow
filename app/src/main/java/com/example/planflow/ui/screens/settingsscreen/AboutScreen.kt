package com.example.planflow.ui.screens.settingsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planflow.BuildConfig
import com.example.planflow.R
import com.example.planflow.ui.components.topbar.TopBar
import com.example.planflow.ui.navigation.AppNavigator

@Composable
fun AboutScreen(navigator: AppNavigator) {
    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(R.string.setting_item_name_about),
                showBackIcon = true,
                onBackClick = { navigator.goBack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Icon Placeholder
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "${stringResource(R.string.setting_item_version)} ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = stringResource(R.string.about_app_description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            HorizontalDivider()
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.about_developer_label)) },
                supportingContent = { Text(stringResource(R.string.about_developer_name)) }
            )
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.about_privacy_policy)) },
                modifier = Modifier.fillMaxWidth() // Add clickable if needed
            )
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.about_terms_service)) },
                modifier = Modifier.fillMaxWidth() // Add clickable if needed
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} PlanFlow",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
