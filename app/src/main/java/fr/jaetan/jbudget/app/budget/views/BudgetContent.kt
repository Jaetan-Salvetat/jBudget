package fr.jaetan.jbudget.app.budget.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.ui.widgets.BudgetChart

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BudgetContent(padding: PaddingValues, viewModel: BudgetViewModel, navController: NavHostController) {
    LazyColumn(Modifier.padding(padding)) {
        item { GraphicWidget(viewModel = viewModel) }

        stickyHeader { TransactionTitleSection() }

        items(viewModel.transactions) {
            Divider()
            TransactionItem(it, viewModel, navController)
        }
    }
}

@Composable
private fun GraphicWidget(viewModel: BudgetViewModel) {
    BudgetChart(viewModel.transactions)
}



@Composable
private fun TransactionTitleSection() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp, 10.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_transactions),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {  }) {
                Icon(Icons.Default.FilterList, stringResource(R.string.filter_transactions_descriptor))
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction, viewModel: BudgetViewModel, navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }
    val category = viewModel.categories.find { it.id == transaction.categoryId }

    Box(modifier = Modifier
        .clickable { viewModel.navigateToUpdateTransactionScreen(navController, transaction) }
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 10.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transaction.amount.toString(),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = transaction.date.toText(),
                style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.outline))
            Spacer(Modifier.weight(1f))
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(25.dp))) {
                Text(
                    modifier = Modifier.padding(5.dp, 2.dp),
                    text = category?.name ?: stringResource(id = R.string.no_category_assigned),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
            Column {
                IconButton(onClick = { isExpanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_vert_descriptor))
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource( R.string.transaction_edit)) },
                        onClick = { viewModel.navigateToUpdateTransactionScreen(navController, transaction) }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource( R.string.transaction_delete), color = MaterialTheme.colorScheme.errorContainer) },
                        onClick = {
                            viewModel.removeTransaction(transaction)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}