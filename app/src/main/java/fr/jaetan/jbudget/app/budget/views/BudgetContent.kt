package fr.jaetan.jbudget.app.budget.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel

@Composable
fun BudgetContent(padding: PaddingValues, viewModel: BudgetViewModel) {
    LazyColumn(Modifier.padding(padding)) {
        item { /* TODO GRAPHIC WIDGET */ }
        item { BudgetDates(viewModel) }
        item { BudgetCategories() }
        items(listOf<Any>()) {

        }
    }
}

@Composable
fun BudgetDates(viewModel: BudgetViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()) {


        Text(
            text = viewModel.budget!!.startDate.toString(),
            style = MaterialTheme.typography.titleMedium)
        Text(
            text = viewModel.budget!!.endDate.toString(),
            style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun BudgetCategories() {

    val categories = listOf(
        stringResource(R.string.sample_budget_category_name1),
        stringResource(R.string.sample_budget_category_name2),
        stringResource(R.string.sample_budget_category_name3))

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()) {
        items(categories) { category ->
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(
                    text = category)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        item {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = stringResource(R.string.expand_section_descriptor))
            }
        }
    }
}