package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.JBudget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveBudgetDialog(isVisible: Boolean, budget: Budget, onRemove: () -> Unit = {}, dismiss: () -> Unit) {
    if (isVisible) {
        var isLoading by remember { mutableStateOf(false) }

        AlertDialog(onDismissRequest = dismiss) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
                Row {
                    Text(
                        text = "Êtes vous sûr de vouloir supprimer le budget ",
                        modifier = Modifier.padding(20.dp)
                    )
                    Text(budget.name)
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = dismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            isLoading = true
                            JBudget.budgetRepository.delete(budget.id) {
                                onRemove()
                                dismiss()
                            }
                        }
                    ) {
                        if (isLoading) CircularProgressIndicator(Modifier.size(10.dp))
                        else Text(stringResource(R.string.remove))
                    }
                }
            }
        }
    }
}