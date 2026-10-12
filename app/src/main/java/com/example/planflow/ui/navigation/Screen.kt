package com.example.planflow.ui.navigation

sealed class Screen(val route: String) {
    companion object {
        const val TRANSACTION_ID_ARGUMENT = "transactionId"
    }
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object About : Screen("about")
    data object AddTransaction : Screen("add_transaction")
    data object Details : Screen("details/{$TRANSACTION_ID_ARGUMENT}") {
        fun createRoute(id: String) = "details/$id"
    }
    data object TransactionEditor : Screen("transaction_editor/{$TRANSACTION_ID_ARGUMENT}") {
        fun createRoute(id: String) = "transaction_editor/$id"
    }
}
