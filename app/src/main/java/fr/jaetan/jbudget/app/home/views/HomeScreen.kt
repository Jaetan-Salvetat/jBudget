package fr.jaetan.jbudget.app.home.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    Scaffold {
        Column {
            AppBar(navController)
            HomeContent(viewModel)
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(if (viewModel.fabExpanded) MaterialTheme.colorScheme.scrim.copy(alpha = .5f) else Color.Transparent)
        ) {
            HomeFAB(Modifier.align(Alignment.BottomEnd).padding(bottom = 30.dp, end = 30.dp), viewModel)
        }
    }

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
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.home_appbar_settings_descriptor))
            }
        }
    )
}