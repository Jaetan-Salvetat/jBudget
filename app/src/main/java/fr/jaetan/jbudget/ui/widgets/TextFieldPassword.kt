package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.services.isPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldPassword(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
    var hidePassword by rememberSaveable { mutableStateOf(true) }
    
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = if (hidePassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { hidePassword = !hidePassword }) {
                Icon(
                    imageVector = if (hidePassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = stringResource(R.string.password)
                )
            }
        },
        supportingText = {
            if (!value.isPassword && value.isNotEmpty()) {
                Text(stringResource(R.string.bad_password), color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPassword(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    var hidePassword by rememberSaveable { mutableStateOf(true) }

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = if (hidePassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { hidePassword = !hidePassword }) {
                Icon(
                    imageVector = if (hidePassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = stringResource(R.string.password)
                )
            }
        },
        supportingText = {
            if (!value.isPassword && value.isNotEmpty()) {
                Text(stringResource(R.string.bad_password), color = MaterialTheme.colorScheme.error)
            }
        }
    )
}