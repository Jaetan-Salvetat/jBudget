package fr.jaetan.jbudget.app.settings.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.ui.widgets.ForgotPasswordDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SettingsAppBar(scrollBehavior) { navController.popBackStack() } },
        content = { SettingsContent(it, viewModel) }
    )

    ForgotPasswordDialog(viewModel.showResetPasswordDialog) { viewModel.showResetPasswordDialog = false }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppBar(scrollBehavior: TopAppBarScrollBehavior, navigationBack: () -> Unit) {
    LargeTopAppBar(
        title = { Text(stringResource(R.string.my_settings)) },
        navigationIcon = {
            IconButton(onClick = navigationBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.return_to_previous_screen)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}