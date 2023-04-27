package fr.jaetan.jbudget.app.transaction.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.transaction.TransactionViewModel
import fr.jaetan.jbudget.ui.widgets.NewCategoryDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(viewModel: TransactionViewModel) {
    Scaffold(
        topBar = { TransactionAppBar(viewModel.navController) },
        content = { TransactionContent(it, viewModel) }
    )

    NewCategoryDialog(isVisible = viewModel.showCategoryDialog) {
        viewModel.showCategoryDialog = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionAppBar(navController: NavHostController) {
    TopAppBar(
        title = { Text(stringResource(R.string.new_transaction)) },
        navigationIcon = { 
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.return_to_previous_screen)
                )
            }
        }
    )
}