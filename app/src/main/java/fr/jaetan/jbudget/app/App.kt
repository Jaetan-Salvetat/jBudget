package fr.jaetan.jbudget.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel
import fr.jaetan.jbudget.app.budget.views.BudgetScreen
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.app.home.views.HomeScreen
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.app.settings.view.SettingsScreen
import fr.jaetan.jbudget.app.transaction.TransactionViewModel
import fr.jaetan.jbudget.app.transaction.view.TransactionScreen
import fr.jaetan.jbudget.core.models.Screen
import kotlin.system.exitProcess

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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBackHandler(navController: NavHostController) {
    var showQuitDialog by rememberSaveable { mutableStateOf(false) }
    BackHandler {
        if (navController.backQueue.size <= 2) showQuitDialog = true
        else navController.popBackStack()
    }

    if (showQuitDialog) {
        AlertDialog(onDismissRequest = { showQuitDialog = false }) {
            Box(Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
                Column(Modifier.padding(20.dp)) {
                    Text(stringResource(R.string.quit_app_dialog_text))
                    Spacer(Modifier.height(20.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showQuitDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(onClick = { exitProcess(0) }) {
                            Text(stringResource(R.string.quit))
                        }
                    }
                }
            }
        }
        /*AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            text = { Text("Et tu sûr de vouloir quitter l'application ?") },
            confirmButton = {
                TextButton(
                    onClick = { exitProcess(0) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(stringResource(R.string.quit))
                }
            }
        )*/
    }
}