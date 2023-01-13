package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HomeContent(viewModel: HomeViewModel) {
    LazyColumn {
        item { TipsSection(viewModel) }
        item { Divider() }
        items(viewModel.budgets) { HomeBudgetsListItem(it, viewModel) }
    }
}