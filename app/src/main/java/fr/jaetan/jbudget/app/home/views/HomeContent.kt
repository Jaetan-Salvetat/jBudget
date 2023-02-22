package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.State

@Composable
fun HomeContent(viewModel: HomeViewModel, modifier: Modifier) {
    Box(modifier) {
        when (viewModel.loadingState) {
            State.Loading -> LoadingContainer(viewModel)
            State.EmptyData -> EmptyDataContainer(viewModel)
            State.None -> HomeBudgetsSection(viewModel)
            else -> ErrorContainer(viewModel)
        }
    }
}

@Composable
private fun LoadingContainer(viewModel: HomeViewModel) {
    Column(Modifier.fillMaxSize()) {
        HomeTipsSection(viewModel)
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ErrorContainer(viewModel: HomeViewModel) {
    Column(Modifier.fillMaxWidth()) {
        HomeTipsSection(viewModel)
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.sample_error),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
private fun EmptyDataContainer(viewModel: HomeViewModel) {
    Column(Modifier.fillMaxSize()) {
        HomeTipsSection(viewModel)
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.empty_budget_create))
            TextButton(onClick = { viewModel.showNewBudgetDialog = true }) {
                Text(stringResource(R.string.home_fab_add_budget))
            }
        }
    }
}