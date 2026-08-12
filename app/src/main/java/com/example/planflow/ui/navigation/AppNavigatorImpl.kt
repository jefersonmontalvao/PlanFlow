package com.example.planflow.ui.navigation

import androidx.navigation.NavHostController
import com.example.planflow.domain.models.Transaction

class AppNavigatorImpl(
    private val navController: NavHostController
) : AppNavigator {

    override fun goToHome() {
        navController.navigate(Screen.Home.route)
    }

    override fun goToSettings() {
        navController.navigate(Screen.Settings.route) {
            popUpTo(Screen.Home.route)
            launchSingleTop = true
        }
    }

    override fun goToAddTransaction() {
        navController.navigate(Screen.AddTransaction.route)
    }

    override fun goToTransactionDetails(transaction: Transaction) {
        navController.navigate(Screen.Details.createRoute(transaction.id))
    }

    override fun goToAbout() {
        navController.navigate(Screen.About.route)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}