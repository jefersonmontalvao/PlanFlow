package com.example.planflow.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planflow.ui.navigation.AppNavigatorImpl
import com.example.planflow.ui.navigation.Screen
import com.example.planflow.ui.screens.AddTransactionScreen
import com.example.planflow.ui.screens.DetailsScreen
import com.example.planflow.ui.screens.settingsscreen.SettingsScreen
import com.example.planflow.ui.screens.TransactionsHistoryScreen
import com.example.planflow.ui.screens.settingsscreen.AboutScreen
import com.example.planflow.ui.theme.PlanFlowTheme
import com.example.planflow.ui.viewmodels.SettingsViewModel
import com.example.planflow.ui.viewmodels.TransactionViewModel

@Composable
fun App() {
    val transactionViewModel: TransactionViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    PlanFlowTheme(
        theme = uiState.theme
    ) {
        val navController = rememberNavController()

        val navigator = remember {
            AppNavigatorImpl(navController)
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(
                route = Screen.Home.route,
            ) {
                TransactionsHistoryScreen(
                    viewModel = transactionViewModel,
                    navigator = navigator
                )
            }

            composable(
                route = Screen.Details.route
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")

                DetailsScreen(
                    viewModel = transactionViewModel,
                    navigator = navigator,
                    transactionId = transactionId
                )
            }

            composable(
                route = Screen.AddTransaction.route
            ) {
                AddTransactionScreen(
                    viewModel = transactionViewModel,
                    navigator = navigator
                )
            }

            composable(
                route = Screen.Settings.route
            ) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    navigator = navigator
                )
            }

            composable(
                route = Screen.About.route
            ) {
                AboutScreen(
                    navigator = navigator
                )
            }
        }
    }
}