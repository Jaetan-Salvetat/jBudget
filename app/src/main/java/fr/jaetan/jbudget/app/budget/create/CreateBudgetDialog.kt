package fr.jaetan.jbudget.app.budget.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.State


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetDialog(navController: NavHostController, dismiss: () -> Unit) {
    val viewModel = CreateBudgetViewModel(dismiss)

    Dialog(onDismissRequest = dismiss) {
        val focusRequest = FocusRequester()
        SideEffect { focusRequest.requestFocus() }

        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(R.string.new_dialog_name), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = viewModel.newBudgetValue,
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                label = { Text(text = stringResource(R.string.new_budget_dialog_name)) },
                onValueChange = { viewModel.newBudgetValue = it },
                supportingText = {
                    if (viewModel.newBudgetError != null) { Text(stringResource(viewModel.newBudgetError!!), color = MaterialTheme.colorScheme.error)}
                },
                modifier = Modifier.focusRequester(focusRequest)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.newBudgetState = State.Loading
                    viewModel.createBudget(viewModel.newBudgetValue, navController)
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
}
