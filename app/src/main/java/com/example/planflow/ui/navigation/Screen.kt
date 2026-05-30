package com.example.planflow.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object AddTransaction : Screen("add_transaction")
    data object Details : Screen("details")
}