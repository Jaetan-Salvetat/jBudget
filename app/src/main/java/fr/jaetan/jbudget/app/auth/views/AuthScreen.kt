package fr.jaetan.jbudget.app.auth.views

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.app.auth.AuthScreens
import fr.jaetan.jbudget.app.auth.AuthViewModel
import fr.jaetan.jbudget.core.models.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {
    val viewModel = AuthViewModel()

    Scaffold(
        topBar = { AuthTopAppBar(viewModel.currentScreen.titleRes) },
        content = { AuthContent(it, viewModel) },
        bottomBar = { BottomButton(viewModel) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthTopAppBar(@StringRes titleRes: Int) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) }
    )
}

@Composable
private fun BottomButton(viewModel: AuthViewModel) {
    Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.clickable {
            if (viewModel.currentScreen == AuthScreens.Login) viewModel.navigate(AuthScreens.Register)
            else viewModel.navigate(AuthScreens.Login)
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