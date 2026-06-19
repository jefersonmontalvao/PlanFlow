package com.example.planflow.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        val THEME_KEY = stringPreferencesKey("theme")
    }

    val themeFlow: Flow<Theme> =
        settingsDataStore.data.map { preferences ->
            Theme.valueOf(
                preferences[THEME_KEY] ?: Theme.SYSTEM.name
            )
        }

    suspend fun setTheme(theme: Theme) {
        settingsDataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}