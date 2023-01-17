package fr.jaetan.jbudget.app.budget.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(viewModel: BudgetViewModel, navController: NavHostController) {

    if (viewModel.budget == null) {
        /* TODO: Afficher une dialog "Budget introuvable + navigation.back */
        return
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { BudgetContent(it, viewModel) },
        topBar = { BudgetAppBar(viewModel, scrollBehavior, navController) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetAppBar(viewModel: BudgetViewModel, scrollBehavior: TopAppBarScrollBehavior, navController: NavHostController) {
    LargeTopAppBar(
        title = { Text(text = viewModel.budget!!.name) },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_descriptor))
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.return_to_previous_screen)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}