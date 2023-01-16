package fr.jaetan.jbudget.app.home.views

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.create.CreateBudgetDialog
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.services.JBudget

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val backgroundAnimation by animateColorAsState(
        targetValue =  if (viewModel.fabExpanded) MaterialTheme.colorScheme.scrim.copy(alpha = .5f) else Color.Transparent
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { HomeFAB(viewModel) }
    ) {
        Column {
            AppBar(scrollBehavior, navController)
            HomeContent(viewModel, Modifier.weight(1f))
        }
        Box(
            Modifier
                .fillMaxSize(if (viewModel.fabExpanded) 1f else 0f)
                .background(backgroundAnimation)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                ) { if (viewModel.fabExpanded) viewModel.fabExpanded = false }
        )
    }

    if (viewModel.showNewBudgetDialog) {
        CreateBudgetDialog(navController) { viewModel.showNewBudgetDialog = false }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(scrollBehavior: TopAppBarScrollBehavior, navController: NavHostController) {
    LargeTopAppBar(
        title = { Text(text = stringResource(R.string.home_welcome, JBudget.state.currentUser!!.displayName!!)) },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.home_appbar_settings_descriptor))
            }
        }
    )
}