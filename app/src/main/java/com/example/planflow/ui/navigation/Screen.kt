package com.example.planflow.ui.navigation

private const val TRANSACTION_ID = "transactionId"

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object AddTransaction : Screen("add_transaction")
    data object Details : Screen("details/{$TRANSACTION_ID}") {
        fun createRoute(id: String) = "details/$id"
    }
}