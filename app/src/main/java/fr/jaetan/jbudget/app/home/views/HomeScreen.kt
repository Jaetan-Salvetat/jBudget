package fr.jaetan.jbudget.app.home.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController, ) {
    val viewModel = HomeViewModel(navController)
    Scaffold( content = {HomeContent(it)}, floatingActionButton = {HomeFAB(viewModel)}, topBar = { AppBar(navController) })
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {navController.navigate("settings")}) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.home_appbar_settings_descriptor))
            }
        }
    )
}