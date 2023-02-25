package fr.jaetan.jbudget.app.budget._budget.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget._budget.BudgetViewModel
import fr.jaetan.jbudget.app.budget.create.CreateBudgetDialog
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.ui.widgets.RemoveBudgetDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(viewModel: BudgetViewModel, navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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

    viewModel.budgetToEdit?.let {
        CreateBudgetDialog(budget = viewModel.budgetToEdit) {
            viewModel.budgetToEdit = null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetAppBar(viewModel: BudgetViewModel, scrollBehavior: TopAppBarScrollBehavior, navController: NavHostController) {
    TopAppBar(
        title = { Text(text = viewModel.budget!!.name) },
        actions = {
            TopAppBarMenu(viewModel)
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
private fun TopAppBarMenu(viewModel: BudgetViewModel) {
    var showDropDown by rememberSaveable {  mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        IconButton(onClick = { showDropDown = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = {
                    JBudget.budgetRepository.shareAsText(context, viewModel.budget!!)
                    showDropDown = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = {
                    viewModel.budgetToEdit = viewModel.budget
                    showDropDown = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.remove)) },
                onClick = {
                    viewModel.budgetToRemove = viewModel.budget
                    showDropDown = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

/*
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
}*/
