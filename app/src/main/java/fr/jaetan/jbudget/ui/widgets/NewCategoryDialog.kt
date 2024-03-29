package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.isCategoryName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategoryDialog(isVisible: Boolean, dismiss: () -> Unit) {
    if (isVisible) {
        var name by rememberSaveable { mutableStateOf("") }
        var loadingState by rememberSaveable { mutableStateOf(State.None) }
        val save = {
            loadingState = State.Loading
            JBudget.categoryRepository.createCategory(Category(name = name)) { dismiss() }
        }

        Dialog(onDismissRequest = dismiss) {
            Box(Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.new_category), style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.new_category)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { save() })
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = save,
                        enabled = loadingState == State.None && name.isCategoryName
                    ) {
                        if (loadingState == State.Loading) {
                            CircularProgressIndicator(Modifier.size(20.dp))
                        } else {
                            Text(stringResource(R.string.next))
                        }
                    }
                }
            }
        }
    }
}