package com.example.planflow.ui.navigation

import com.example.planflow.domain.models.Transaction

interface AppNavigator {
    fun goToHome()

    fun goToSettings()

    fun goToAddTransaction()

    fun goToTransactionDetails(transaction: Transaction)

    fun goBack()
}