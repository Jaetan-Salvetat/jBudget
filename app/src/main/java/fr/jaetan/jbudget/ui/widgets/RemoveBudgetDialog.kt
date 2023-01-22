package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Budget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveBudgetDialog(isVisible: Boolean, budget: Budget, dismiss: () -> Unit) {
    if (isVisible) {
        AlertDialog(onDismissRequest = dismiss) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
                Text(
                    text = "Êtes vous sûr de vouloir supprimer le budget ${budget.name}",
                    modifier = Modifier.padding(20.dp)
                )
                Row(Modifier.fillMaxWidth().padding(end = 10.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = dismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = dismiss) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        }
    }
}