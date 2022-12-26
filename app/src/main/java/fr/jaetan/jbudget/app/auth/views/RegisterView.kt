package fr.jaetan.jbudget.app.auth.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RegisterView(viewModel: AuthViewModel) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()
    val coroutineScope = rememberCoroutineScope()
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
            value = viewModel.email.orEmpty(),
            onValueChange = { viewModel.email = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            keyboardActions = keyboardActions,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    coroutineScope.launch {
                        delay(200)
                        bringIntoViewRequester.bringIntoView()
                    }
                },
            colors = colors,
            supportingText = {
                if (!viewModel.email.isEmail) {
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
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    coroutineScope.launch {
                        delay(200)
                        bringIntoViewRequester.bringIntoView()
                    }
                },
            colors = colors,
            showErrorMessage = !viewModel.password.isPassword
        )
        Spacer(Modifier.bringIntoViewRequester(bringIntoViewRequester))
    }
}