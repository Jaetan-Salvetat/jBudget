package fr.jaetan.jbudget.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.jaetan.jbudget.app.budget._budget.BudgetViewModel
import fr.jaetan.jbudget.app.budget._budget.views.BudgetScreen
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.app.home.views.HomeScreen
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.app.settings.view.SettingsScreen
import fr.jaetan.jbudget.app.transaction.TransactionViewModel
import fr.jaetan.jbudget.app.transaction.view.TransactionScreen
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.ui.widgets.AppBackHandler

@Composable
fun App() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )
    }

    AppBackHandler(navController)

    NavHost(navController, Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(HomeViewModel(navController), navController)
        }
        composable(Screen.Settings.route) {
            val settingsViewModel = SettingsViewModel()
            SettingsScreen(navController, settingsViewModel)
        }
        composable("${Screen.Budget.route}/{budgetId}") {
            val budgetId = it.arguments?.getString("budgetId")
            val budgetViewModel = BudgetViewModel(budgetId)
            BudgetScreen(budgetViewModel, navController)
        }
        composable(Screen.Transaction.route) {
            val transactionViewModel = TransactionViewModel(navController)
            TransactionScreen(transactionViewModel)
        }
        composable("${Screen.Transaction.route}/{transactionId}") {
            val transactionId = it.arguments?.getString("transactionId")
            val transactionViewModel = TransactionViewModel(
                navController = navController,
                transactionId = transactionId
            )

            TransactionScreen(transactionViewModel)
        }
    }
}