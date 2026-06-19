package com.example.planflow.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.planflow.data.repositories.SettingsRepository
import com.example.planflow.domain.models.Theme
import com.example.planflow.testutils.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class SettingsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var settingsDataStore: DataStore<Preferences>
    private lateinit var testFile: File
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        testFile = File.createTempFile(
            "test",
            ".preferences_pb"
        ).apply { deleteOnExit() }

        settingsDataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher + Job())
        ) {
            testFile
        }

        settingsRepository = SettingsRepository(settingsDataStore)
        settingsViewModel = SettingsViewModel(settingsRepository)
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun checkDefaultTheme_ReturnsDefaultTheme() = runTest {
        val defaultTheme = Theme.SYSTEM
        val result = settingsViewModel.uiState.first()

        assertEquals(defaultTheme, result.theme)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun changeTheme_UpdatesThemeCorrectly() = runTest {
        val collectJob = launch {
            settingsViewModel.uiState.collect {}
        }

        val newTheme = Theme.LIGHT

        settingsViewModel.updateTheme(newTheme)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            newTheme,
            settingsViewModel.uiState.value.theme)

        collectJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun themeStateIsSaved() = runTest {
        val savedTheme = Theme.DARK
        settingsViewModel.updateTheme(savedTheme)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(savedTheme, settingsRepository.themeFlow.first())
    }
}