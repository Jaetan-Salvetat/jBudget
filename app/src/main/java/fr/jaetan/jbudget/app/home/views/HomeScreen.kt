package fr.jaetan.jbudget.app.home.views

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.create.CreateBudgetDialog
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.State

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    Scaffold( content = {HomeContent()}, floatingActionButton = {HomeFAB(viewModel)}, topBar = { AppBar(navController) })
    if (viewModel.showNewBudgetDialog) {
        CreateBudgetDialog(viewModel) {
            viewModel.newBudgetValue = ""
            viewModel.newBudgetError = null
            viewModel.newBudgetState = State.None
            viewModel.showNewBudgetDialog = false
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(navController: NavHostController) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {navController.navigate(Screen.Settings.route)}) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.home_appbar_settings_descriptor))
            }
        }
    )
}