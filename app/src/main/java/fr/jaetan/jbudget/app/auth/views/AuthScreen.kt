package fr.jaetan.jbudget.app.auth.views

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import fr.jaetan.jbudget.app.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthScreen() {
    val viewModel = AuthViewModel()

    Scaffold { AuthContent(viewModel) }
}