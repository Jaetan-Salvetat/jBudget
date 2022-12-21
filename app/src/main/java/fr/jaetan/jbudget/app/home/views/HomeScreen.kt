package fr.jaetan.jbudget.app.home.views

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.app.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController, ) {
    val viewModel = HomeViewModel()

    Scaffold( content = {HomeContent()}, floatingActionButton = {HomeFAB(viewModel)})
}
