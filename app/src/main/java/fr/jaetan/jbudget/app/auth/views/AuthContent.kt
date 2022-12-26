package fr.jaetan.jbudget.app.auth.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.AuthScreens
import fr.jaetan.jbudget.app.auth.AuthViewModel
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword
import fr.jaetan.jbudget.core.services.extentions.isUsename

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthContent(padding: PaddingValues, viewModel: AuthViewModel) {
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

    Column(
        Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(70.dp))
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(Modifier.height(70.dp))

        when (viewModel.currentScreen) {
            AuthScreens.Login -> LoginView(viewModel, keyboardActions, colors)
            AuthScreens.Register -> RegisterView(viewModel, keyboardActions, colors)
        }
    }
}

@Composable
fun LoginView(viewModel: AuthViewModel, keyboardActions: KeyboardActions, colors: TextFieldColors) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Input(
            value = viewModel.email.orEmpty(),
            onChange = { viewModel.email = it },
            labelRes = R.string.email,
            supportingText = R.string.bad_email,
            showSupportingText = viewModel.email?.isEmail == false,
            keyboardActions = keyboardActions,
            colors = colors
        )

        PasswordInput(
            value = viewModel.password.orEmpty(),
            onChange = { viewModel.password = it },
            keyboardActions = keyboardActions,
            colors = colors,
            showSupportingText = viewModel.password?.isPassword == false
        )

        Spacer(Modifier.height(20.dp))

        Text(stringResource(
            R.string.forgot_password),
            modifier = Modifier.clickable {  },
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun RegisterView(viewModel: AuthViewModel, keyboardActions: KeyboardActions, colors: TextFieldColors) {
    Column {
        Input(
            value = viewModel.email.orEmpty(),
            onChange = { viewModel.email = it },
            labelRes = R.string.email,
            supportingText = R.string.bad_email,
            showSupportingText = viewModel.email?.isEmail == false,
            keyboardActions = keyboardActions,
            colors = colors
        )

        Input(
            value = viewModel.username.orEmpty(),
            onChange = { viewModel.username = it },
            labelRes = R.string.username,
            supportingText = R.string.bad_username,
            showSupportingText = viewModel.username?.isUsename == false,
            keyboardActions = keyboardActions,
            colors = colors
        )

        PasswordInput(
            value = viewModel.password.orEmpty(),
            onChange = { viewModel.password = it },
            keyboardActions = keyboardActions,
            colors = colors,
            showSupportingText = viewModel.password?.isPassword == false
        )
    }
}