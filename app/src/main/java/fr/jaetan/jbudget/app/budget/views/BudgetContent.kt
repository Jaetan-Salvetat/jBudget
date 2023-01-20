package fr.jaetan.jbudget.app.budget.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.budget.BudgetViewModel
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.ui.widgets.BudgetChart

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BudgetContent(padding: PaddingValues, viewModel: BudgetViewModel, navController: NavHostController) {
    LazyColumn(Modifier.padding(padding)) {
        item { GraphicWidget(viewModel = viewModel) }
        item { BudgetDates(viewModel) }
        item { BudgetCategories(viewModel) }

        stickyHeader { TransactionTitleSection() }

        when (viewModel.transactionLoadingState) {
            State.None -> items(viewModel.transactions) {
                Divider()
                TransactionItem(it, viewModel, navController)
            }
            State.Loading -> item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            State.EmptyData -> item {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.empty_transactions))
                    TextButton(onClick = {  }) {
                        Text(stringResource(R.string.new_transaction))
                    }
                }
            }
            else -> item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.sample_error))
                }
            }
        }
    }
}

@Composable
private fun GraphicWidget(viewModel: BudgetViewModel) {
    BudgetChart(transactions = viewModel.transactions)
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
    Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
        Text(
            text = if (viewModel.budget!!.endDate != null)
                viewModel.budget!!.endDate!!.toText() else
                stringResource(id = R.string.actually),
            style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun BudgetCategories(viewModel: BudgetViewModel) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()) {
            items(viewModel.categories) { category ->
                Box(
                    Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { }
                ) {
                    Text(
                        text = category.name,
                        modifier = Modifier.padding(15.dp, 10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(Modifier.width(10.dp))
                
            }
            item {
                Spacer(Modifier.width(45.dp))
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
            IconButton(onClick = { viewModel.showNewCategoryDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.expand_section_descriptor)
                )
            }
        }
    }
}


@Composable
private fun TransactionTitleSection() {
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        Row(
            Modifier.fillMaxWidth().padding(20.dp, 10.dp, 10.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_transactions),
                style = MaterialTheme.typography.titleMedium,
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
                if (transaction.categoryId != null) {
                    Text(
                        modifier = Modifier.padding(5.dp, 2.dp),
                        text = stringResource(id = R.string.no_category_assigned),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer)
                } else {
                    Text(
                        modifier = Modifier.padding(5.dp, 2.dp),
                        text = stringResource(id = R.string.no_category_assigned),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer)
                }
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
                        onClick = { viewModel.removeTransaction(transaction) })
                }
            }
        }
    }
}