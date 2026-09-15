package com.example.planflow.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.planflow.domain.models.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) {
    private companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val IS_NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("is_notifications_enabled")
    }

    val themeFlow: Flow<Theme> =
        settingsDataStore.data.map { preferences ->
            Theme.valueOf(
                preferences[THEME_KEY] ?: Theme.SYSTEM.name
            )
        }

    val isNotificationsEnabledFlow: Flow<Boolean> =
        settingsDataStore.data.map { preferences ->
            preferences[IS_NOTIFICATIONS_ENABLED_KEY] ?: false
        }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[IS_NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    suspend fun setTheme(theme: Theme) {
        settingsDataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}