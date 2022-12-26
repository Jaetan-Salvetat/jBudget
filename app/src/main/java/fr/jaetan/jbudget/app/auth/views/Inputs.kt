package fr.jaetan.jbudget.app.auth.views

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.ui.widgets.OutlinedTextFieldPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    val bringIntoViewRequester = BringIntoViewRequester()
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
        keyboardActions = keyboardActions,
        label = { Text(stringResource(labelRes)) },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                coroutineScope.launch {
                    delay(1000)
                    bringIntoViewRequester.bringIntoView()
                }
            }
            .onFocusEvent {
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            },
        colors = colors,
        supportingText = {
            if (showSupportingText) {
                Text(stringResource(supportingText), color = MaterialTheme.colorScheme.error)
            }
        }
    )
    Spacer(Modifier.height(15.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PasswordInput(
    value: String,
    onChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    colors: TextFieldColors,
    showSupportingText: Boolean
) {
    val bringIntoViewRequester = BringIntoViewRequester()
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextFieldPassword(
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = keyboardActions,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                coroutineScope.launch {
                    delay(500)
                    bringIntoViewRequester.bringIntoView()
                }
            }
            .onFocusEvent {
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            },
        colors = colors,
        showErrorMessage = showSupportingText
    )
    Spacer(Modifier.bringIntoViewRequester(bringIntoViewRequester))
}