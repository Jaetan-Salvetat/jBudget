package fr.jaetan.jbudget.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.jaetan.jbudget.app.budget.BudgetViewModel
import fr.jaetan.jbudget.app.budget.views.BudgetScreen
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.app.home.views.HomeScreen
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.app.settings.view.SettingsScreen
import fr.jaetan.jbudget.app.transaction.view.TransactionScreen
import fr.jaetan.jbudget.core.models.Screen

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

    NavHost(navController, Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(HomeViewModel(navController), navController)
        }
        composable(Screen.Settings.route) {
            val settingsViewModel = SettingsViewModel()
            SettingsScreen(navController, settingsViewModel)
        }
        composable(Screen.Transaction.route) {
            TransactionScreen(navController)
        }
        composable("${Screen.Budget.route}/{budgetId}") {
            val budgetId = it.arguments?.getString("budgetId")
            val budgetViewModel = BudgetViewModel(navController, budgetId)
            BudgetScreen(budgetViewModel, navController)
        }
    }
}