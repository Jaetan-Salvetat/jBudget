package fr.jaetan.jbudget.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetDialog(input: String) {
    val inputState = remember { mutableStateOf(input) }
    Column {
        Text(stringResource(R.string.new_dialog_name))
        TextField(
            value = inputState.value,
            onValueChange = { inputState.value = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {

            }
        ) {
            Text(stringResource(R.string.new_dialog_create))
        }
    }
}
