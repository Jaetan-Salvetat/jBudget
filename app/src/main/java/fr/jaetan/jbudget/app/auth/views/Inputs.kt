package fr.jaetan.jbudget.app.auth.views

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.ui.widgets.OutlinedTextFieldPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Input(
    value: String,
    onChange: (String) -> Unit,
    @StringRes labelRes: Int,
    keyboardActions: KeyboardActions,
    colors: TextFieldColors,
    showSupportingText: Boolean,
    @StringRes supportingText: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
        keyboardActions = keyboardActions,
        label = { Text(stringResource(labelRes)) },
        modifier = Modifier.fillMaxWidth(),
        colors = colors,
        supportingText = {
            if (showSupportingText) {
                Text(stringResource(supportingText), color = MaterialTheme.colorScheme.error)
            }
        }
    )
    Spacer(Modifier.height(15.dp))
}

@Composable
internal fun PasswordInput(
    value: String,
    onChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    colors: TextFieldColors,
    showSupportingText: Boolean
) {
    OutlinedTextFieldPassword(
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = keyboardActions,
        modifier = Modifier.fillMaxWidth(),
        colors = colors,
        showErrorMessage = showSupportingText
    )
}