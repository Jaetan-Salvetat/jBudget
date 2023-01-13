package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HomeContent(viewModel: HomeViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        item { TipsSection(viewModel) }
        item { Divider() }
        items(viewModel.currentBudgets) { budget ->
            HomeBudgetsListItem(
                budget,
                viewModel.selectedCurrentBudgets.find { it == budget } != null,
                viewModel
            )
        }
        item { Divider() }
        items(viewModel.oldBudgets) { HomeBudgetsListItem(it, viewModel.selectedOldBudget == it, viewModel) }
        item { Spacer(Modifier.height(100.dp)) }
    }
}