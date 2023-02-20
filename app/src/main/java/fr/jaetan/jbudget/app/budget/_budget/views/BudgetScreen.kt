package fr.jaetan.jbudget.app.budget._budget.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget._budget.BudgetViewModel
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.ui.widgets.RemoveBudgetDialog

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
        content = { BudgetContent(it, viewModel, navController) },
        topBar = { BudgetAppBar(viewModel, scrollBehavior, navController) })


    viewModel.budgetToRemove?.let {
        RemoveBudgetDialog(
            isVisible = viewModel.budgetToRemove != null,
            budget = it,
            onRemove = { navController.popBackStack() }
        ) { viewModel.budgetToRemove = null }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetAppBar(viewModel: BudgetViewModel, scrollBehavior: TopAppBarScrollBehavior, navController: NavHostController) {
    LargeTopAppBar(
        title = { Text(text = viewModel.budget!!.name) },
        actions = {
            BudgetDates(viewModel = viewModel)
            IconButton(onClick = { viewModel.budgetToRemove = viewModel.budget }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.budget_delete),
                    tint = MaterialTheme.colorScheme.errorContainer
                )
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

@Composable
private fun BudgetDates(viewModel: BudgetViewModel) {
    Text(
        text = viewModel.budget!!.startDate.toText(),
        style = MaterialTheme.typography.bodySmall)

    Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)

    Text(
        text = if (viewModel.budget!!.endDate != null)
            viewModel.budget!!.endDate!!.toText() else
            stringResource(id = R.string.actually),
        style = MaterialTheme.typography.bodySmall)
}