package com.example.planflow.ui.navigation

import androidx.navigation.NavHostController

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

    override fun goBack() {
        navController.popBackStack()
    }
}