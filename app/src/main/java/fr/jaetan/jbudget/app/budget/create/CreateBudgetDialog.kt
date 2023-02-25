package fr.jaetan.jbudget.app.budget.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.ui.widgets.dateSelector


@Composable
fun CreateBudgetDialog(budget: Budget? = null, dismiss: () -> Unit) {
    val viewModel = CreateBudgetViewModel(budget, dismiss)

    Dialog(onDismissRequest = dismiss) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
        ) {
            CreateBudgetHeader(viewModel)
            CreateBudgetFooter(viewModel)
            CreateBudgetButtons(viewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateBudgetHeader(viewModel: CreateBudgetViewModel) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = if (viewModel.isInEditMode) stringResource(R.string.edit_budget)
                    else stringResource(R.string.new_dialog_name),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = viewModel.newBudgetValue,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            label = { Text(text = stringResource(R.string.new_budget_dialog_name)) },
            onValueChange = { viewModel.newBudgetValue = it },
            supportingText = {
                if (viewModel.newBudgetError != null) { Text(stringResource(viewModel.newBudgetError!!), color = MaterialTheme.colorScheme.error)}
            },
        )
    }
}

@Composable
private fun CreateBudgetFooter(viewModel: CreateBudgetViewModel) {
    val context = LocalContext.current

    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    dateSelector(context, viewModel.startDate) { viewModel.startDate = it }
                }
        ) {
            Text(
                viewModel.startDate.toText(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
        Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
        if (viewModel.endDate != null) {
            Row(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        dateSelector(context, viewModel.endDate!!, viewModel.startDate) { viewModel.endDate = it }
                    }
            ) {
                Text(
                    viewModel.endDate!!.toText(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        } else {
            Row(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        dateSelector(context, viewModel.startDate, viewModel.startDate) { viewModel.endDate = it }
                    }
            ) {
                Text(
                    stringResource(R.string.end_date),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun CreateBudgetButtons(viewModel: CreateBudgetViewModel) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (viewModel.isInEditMode) viewModel.editBudget()
                else viewModel.createBudget()
            },
            enabled = when {
                viewModel.newBudgetState == State.Loading -> false
                viewModel.newBudgetValue.isBlank() -> false
                else -> true
            }
        ) {
            when (viewModel.newBudgetState) {
                State.Loading -> CircularProgressIndicator(Modifier.size(20.dp))
                else -> Text(stringResource(R.string.new_dialog_create))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}