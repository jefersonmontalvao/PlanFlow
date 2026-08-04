package com.example.planflow.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planflow.ui.screens.HomeScreen
import com.example.planflow.ui.viewmodels.TransactionViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val transactionViewModel = hiltViewModel<TransactionViewModel>()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(
            route = "home",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() }
        ) {
            HomeScreen(
                viewModel = transactionViewModel
            )
        }
    }
}