package fr.jaetan.jbudget.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.jaetan.jbudget.app.home.views.HomeScreen
import fr.jaetan.jbudget.app.settings.view.SettingsScreen
import fr.jaetan.jbudget.app.transaction.view.TransactionScreen
import fr.jaetan.jbudget.core.models.Screen

@Composable
fun App() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val backgroundColor = MaterialTheme.colorScheme.background
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = darkTheme
        )
        systemUiController.setSystemBarsColor(
            color = backgroundColor
        )
    }

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