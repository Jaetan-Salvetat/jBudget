package fr.jaetan.jbudget.app.settings.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword
import fr.jaetan.jbudget.ui.widgets.OutlinedTextFieldPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEmailDialog(viewModel: SettingsViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            viewModel.updateEmail()
            focusManager.clearFocus()
        }
    )

    if (viewModel.showUpdateEmailDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissUpdateEmailDialog,
            confirmButton = {
                TextButton(
                    onClick = viewModel::updateEmail,
                    enabled = viewModel.email?.isEmail == true
                            && viewModel.password?.isPassword == true
                            && viewModel.updateEmailState != State.Loading
                ) {
                    if (viewModel.updateEmailState == State.Loading) {
                        CircularProgressIndicator(Modifier.size(20.dp))
                    } else {
                        Text(stringResource(R.string.next))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissUpdateEmailDialog) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.update_my_email))
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)) {
                        viewModel.updateEmailErrorMessageRes?.let{
                            Text(
                                stringResource(it),
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = viewModel.email.orEmpty(),
                        onValueChange = { viewModel.email = it },
                        label = { Text(stringResource(R.string.new_email)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = keyboardActions
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextFieldPassword(
                        value = viewModel.password.orEmpty(),
                        onValueChange = { viewModel.password = it },
                        showErrorMessage = viewModel.password?.isPassword == false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = keyboardActions
                    )
                }
            }
        )
    }
}