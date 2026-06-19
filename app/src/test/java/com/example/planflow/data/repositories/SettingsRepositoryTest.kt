package com.example.planflow.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.planflow.domain.models.Theme
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class SettingsRepositoryTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepository
    private lateinit var testFile: File

    @Before
    fun setup() {
        testFile = File.createTempFile(
            "test",
            ".preferences_pb"
        ).apply {
            deleteOnExit()
        }

        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO)
        ) {
            testFile
        }

        repository = SettingsRepository(dataStore)
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun setTheme_savesThemeCorrectly() = runTest {
        repository.setTheme(Theme.DARK)

        val result = repository.themeFlow.first()
        assertEquals(Theme.DARK, result)
    }

    @Test
    fun checkDefaultTheme_returnsDefaultTheme() = runTest {
        val defaultTheme = Theme.SYSTEM
        val result = repository.themeFlow.first()

        assertEquals(defaultTheme, result)
    }
}