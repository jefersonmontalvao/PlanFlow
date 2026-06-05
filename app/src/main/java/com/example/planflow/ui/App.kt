package com.example.planflow.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planflow.ui.navigation.AppNavigatorImpl
import com.example.planflow.ui.navigation.Screen
import com.example.planflow.ui.screens.AddTransactionScreen
import com.example.planflow.ui.screens.DetailsScreen
import com.example.planflow.ui.screens.TransactionsHistoryScreen
import com.example.planflow.ui.theme.PlanFlowTheme
import com.example.planflow.ui.viewmodels.TransactionViewModel

@Composable
fun App() {
    PlanFlowTheme {
        val navController = rememberNavController()

        val navigator = remember {
            AppNavigatorImpl(navController)
        }

        val transactionViewModel: TransactionViewModel = hiltViewModel()

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
                // TODO
            }
        }
    }
}