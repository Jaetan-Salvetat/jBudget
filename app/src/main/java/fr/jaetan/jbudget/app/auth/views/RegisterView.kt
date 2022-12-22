package fr.jaetan.jbudget.app.auth.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.AuthViewModel
import fr.jaetan.jbudget.ui.widgets.OutlinedTextFieldPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(viewModel: AuthViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            viewModel.register()
            focusManager.clearFocus()
        }
    )
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = Color.Transparent,
    )

    Column {
        OutlinedTextField(
            value = viewModel.registerEmail,
            onValueChange = { viewModel.registerEmail = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            keyboardActions = keyboardActions,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
        Spacer(Modifier.height(30.dp))
        OutlinedTextFieldPassword(
            value = viewModel.registerPassword,
            onValueChange = { viewModel.registerPassword = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
    }
}