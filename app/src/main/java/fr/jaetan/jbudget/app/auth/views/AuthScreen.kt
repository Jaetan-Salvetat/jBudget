package fr.jaetan.jbudget.app.auth.views

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.AuthScreens
import fr.jaetan.jbudget.app.auth.AuthViewModel
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.ui.widgets.ForgotPasswordDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController, viewModel: AuthViewModel) {
    Scaffold(
        topBar = { AuthTopAppBar(viewModel.currentScreen.titleRes, navController, viewModel) },
        content = { AuthContent(it, navController, viewModel) },
        bottomBar = { BottomButton(viewModel, navController) }
    )

    ForgotPasswordDialog(isVisible = viewModel.showForgotPasswordDialog) {
        viewModel.showForgotPasswordDialog = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthTopAppBar(@StringRes titleRes: Int, navController: NavHostController, viewModel: AuthViewModel) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        navigationIcon = {
            if (viewModel.currentScreen == AuthScreens.Register) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.return_to_previous_screen)
                    )
                }
            }
        }
    )
}

@Composable
private fun BottomButton(viewModel: AuthViewModel, navController: NavHostController) {
    Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.clickable {
            if (viewModel.currentScreen == AuthScreens.Login) navController.navigate(AuthScreens.Register.route)
            else navController.popBackStack()
        }) {
            Text("${stringResource(viewModel.currentScreen.navigationTextRes)} ")
            Text(stringResource(viewModel.currentScreen.navigationTextActionRes), color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(20.dp))
        
        Button(
            onClick = { viewModel.auth() },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.canContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            if (viewModel.state == State.Loading) {
                CircularProgressIndicator(Modifier.size(20.dp))
            } else {
                Text(stringResource(viewModel.currentScreen.titleRes))
            }
        }
    }
}