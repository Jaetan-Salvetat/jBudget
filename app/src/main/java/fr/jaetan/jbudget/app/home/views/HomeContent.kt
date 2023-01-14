package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
            State.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            State.EmptyData -> EmptyDataContainer(Modifier.align(Alignment.Center), viewModel)
            State.None -> HomeBudgetsSection(viewModel)
            else -> {}
        }
    }
}

@Composable
private fun EmptyDataContainer(modifier: Modifier, viewModel: HomeViewModel) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HomeTipsSection(viewModel)
        Divider()
        Spacer(Modifier.weight(1f))
        Text("Ta liste de budget est vide, ajoute en !")
        TextButton(onClick = { viewModel.showNewBudgetDialog = true }) {
            Text(stringResource(R.string.home_fab_add_budget))
        }
        Spacer(Modifier.weight(1f))
    }
}