package fr.jaetan.jbudget.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.jaetan.jbudget.app.home.views.HomeScreen
import fr.jaetan.jbudget.app.settings.view.SettingsScreen
import fr.jaetan.jbudget.app.transaction.view.TransactionScreen
import fr.jaetan.jbudget.core.models.Screen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController, Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Transaction.route) {
            TransactionScreen(navController)
        }
    }
}