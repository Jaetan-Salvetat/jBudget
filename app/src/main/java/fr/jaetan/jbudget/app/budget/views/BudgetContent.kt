package fr.jaetan.jbudget.app.budget.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.extentions.toText

@Composable
fun BudgetContent(padding: PaddingValues, viewModel: BudgetViewModel) {
    LazyColumn(Modifier.padding(padding)) {
        item { /* TODO GRAPHIC WIDGET */ }
        item { BudgetDates(viewModel) }
        item { BudgetCategories(viewModel) }
        items(viewModel.transactions) {
            Divider(modifier = Modifier.padding(vertical = 10.dp))
            TransactionItem(it)
        }
    }
}

@Composable
private fun BudgetDates(viewModel: BudgetViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()) {


        Text(
            text = viewModel.budget!!.startDate.toText(),
            style = MaterialTheme.typography.titleMedium)
        viewModel.budget!!.endDate?.let {
            Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
            Text(
                text = it.toText(),
                style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun BudgetCategories(viewModel: BudgetViewModel) {
    val categories = listOf(
        stringResource(R.string.sample_budget_category_name1),
        stringResource(R.string.sample_budget_category_name2),
        stringResource(R.string.sample_budget_category_name2),
        stringResource(R.string.sample_budget_category_name3),
        stringResource(R.string.sample_budget_category_name3))

    Box(Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()) {
            items(categories) { category ->
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = category)
                }
                Spacer(modifier = Modifier.width(10.dp))
                
            }
            item {
                Spacer(modifier = Modifier.width(45.dp))
            }
        }
        Box(
            Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        0.0f to Color.Transparent,
                        0.2f to MaterialTheme.colorScheme.background.copy(0.5f),
                        0.6f to MaterialTheme.colorScheme.background.copy(1f)
                    )
                )
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        ) {
            IconButton(onClick = { viewModel.expandCategories = !viewModel.expandCategories }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.expand_section_descriptor)
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .clickable { }
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp, 10.dp, 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transaction.amount.toString(),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = transaction.date.toText(), 
                style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.outline))
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(25.dp))) {
                if (transaction.categoryId != null) {
                    Text(
                        modifier = Modifier.padding(5.dp, 2.dp),
                        text = stringResource(id = R.string.no_category_assigned),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer)
                } else {
                    Text(
                        modifier = Modifier.padding(5.dp, 2.dp),
                        text = stringResource(id = R.string.no_category_assigned),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer)
                }
            }
            Column {
                IconButton(onClick = { isExpanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_vert_descriptor))
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource( R.string.transaction_delete)) }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = stringResource( R.string.transaction_edit)) }, onClick = { /*TODO*/ })
                }
            }
        }
    }
}