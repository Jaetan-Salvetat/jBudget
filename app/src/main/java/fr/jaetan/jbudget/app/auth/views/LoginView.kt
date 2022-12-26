package fr.jaetan.jbudget.app.auth.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword
import fr.jaetan.jbudget.ui.widgets.OutlinedTextFieldPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(viewModel: AuthViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            viewModel.login()
            focusManager.clearFocus()
        }
    )
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = Color.Transparent,
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = viewModel.email.orEmpty(),
            onValueChange = { viewModel.email = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            keyboardActions = keyboardActions,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            colors = colors,
            supportingText = {
                if (viewModel.email != null && !viewModel.email!!.isEmail) {
                    Text(stringResource(R.string.bad_email), color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(Modifier.height(30.dp))
        OutlinedTextFieldPassword(
            value = viewModel.password.orEmpty(),
            onValueChange = { viewModel.password = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth(),
            colors = colors,
            showErrorMessage = viewModel.password != null && !viewModel.password!!.isPassword
        )
        
        Spacer(Modifier.height(20.dp))

        Text(stringResource(
            R.string.forgot_password),
            modifier = Modifier.clickable {  },
            color = MaterialTheme.colorScheme.primary
        )
    }
}