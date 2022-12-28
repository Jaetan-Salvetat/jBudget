package fr.jaetan.jbudget.ui.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.isEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    isVisible: Boolean,
    dismiss: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf(null as String?) }
    val focusManager = LocalFocusManager.current

    BackHandler(onBack = dismiss)

    if (isVisible) {
        AlertDialog(
            onDismissRequest = dismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        JBudget.authRepository.resetPassword(email!!)
                        dismiss()
                    },
                    enabled = email?.isEmail == true
                ) {
                    Text(stringResource(R.string.next))
                }
            },
            dismissButton = {
                TextButton(onClick = dismiss) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.password_reset))
            },
            text = {
                TextField(
                    value = email.orEmpty(),
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        )
    }
}