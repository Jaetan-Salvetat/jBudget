package fr.jaetan.jbudget.app.auth.views

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.AuthScreens
import fr.jaetan.jbudget.app.auth.AuthViewModel
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthContent(padding: PaddingValues, navController: NavHostController, viewModel: AuthViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            viewModel.auth()
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
        Spacer(Modifier.height(35.dp))
        if (viewModel.state == State.Error) ErrorContainer(viewModel.errorMessageRes)
        Spacer(Modifier.height(35.dp))


        NavHost(navController, AuthScreens.Login.route) {
            composable(AuthScreens.Login.route) {
                LoginView(viewModel, keyboardActions, colors)
            }
            composable(AuthScreens.Register.route) {
                RegisterView(viewModel, keyboardActions, colors)
            }
        }
    }
}

@Composable
private fun LoginView(viewModel: AuthViewModel, keyboardActions: KeyboardActions, colors: TextFieldColors) {
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
            modifier = Modifier.clickable { viewModel.showForgotPasswordDialog = true },
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RegisterView(viewModel: AuthViewModel, keyboardActions: KeyboardActions, colors: TextFieldColors) {
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

        PasswordInput(
            value = viewModel.password.orEmpty(),
            onChange = { viewModel.password = it },
            keyboardActions = keyboardActions,
            colors = colors,
            showSupportingText = viewModel.password?.isPassword == false
        )
    }
}

@Composable
private fun ErrorContainer(@StringRes messageRes: Int) {
    val shape = RoundedCornerShape(5.dp)

    Box(
        Modifier
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = .6f), shape)
            .border(1.dp, MaterialTheme.colorScheme.errorContainer, shape)
    ) {
        Text(
            stringResource(messageRes),
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(vertical = 25.dp, horizontal = 15.dp)
        )
    }
}