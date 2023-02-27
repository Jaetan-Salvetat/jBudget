package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.JBudget

@Composable
fun RemoveBudgetDialog(isVisible: Boolean, budget: Budget, onRemove: () -> Unit = {}, dismiss: () -> Unit) {
    if (isVisible) {
        var isLoading by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = dismiss,
            text = {
                Text(
                    text = "Êtes vous sûr de vouloir supprimer le budget '${budget.name}'",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            confirmButton = {
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
            },
            dismissButton = {
                TextButton(onClick = dismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}