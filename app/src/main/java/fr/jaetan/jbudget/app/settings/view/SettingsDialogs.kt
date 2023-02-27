package fr.jaetan.jbudget.app.settings.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.views.PasswordInput
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveAccountDialog(viewModel: SettingsViewModel) {
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable {  mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }
    var showErrorBadPassword by rememberSaveable { mutableStateOf(false) }

    val dismiss = {
        isLoading = false
        showError = false
        showErrorBadPassword = false
        password = ""
        viewModel.showRemoveAccountDialog = false
    }

    val removeAccount = {
        showError = false
        isLoading = true
        if (!password.isPassword) {
            showError = true
            isLoading = false
        } else {
            JBudget.userRepository.removeAccount(password) {
                isLoading = false
                if (it == FirebaseResponse.Success) {
                    dismiss()
                } else {
                    showErrorBadPassword = true
                }
            }
        }
    }

    if (viewModel.showRemoveAccountDialog) {
        AlertDialog(
            onDismissRequest = { dismiss() },
            confirmButton = {
                TextButton(onClick = { removeAccount() }, enabled = !isLoading) {
                    if (isLoading) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(15.dp))
                    } else {
                        Text(stringResource(R.string.remove))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { dismiss() }, enabled = !isLoading) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.remove_my_account))
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (showErrorBadPassword) {
                            stringResource(R.string.incorrect_password)
                        } else {
                            ""
                        },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.error,
                            fontStyle = FontStyle.Italic
                        )
                    )

                    PasswordInput(
                        value = password,
                        onChange = { password = it },
                        keyboardActions = KeyboardActions(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent
                        ),
                        showSupportingText = showError
                    )

                    Spacer(Modifier.height(15.dp))

                    Text(
                        text = stringResource(R.string.remove_account_sub_message),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.outline,
                            fontStyle = FontStyle.Italic,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        )
    }
}