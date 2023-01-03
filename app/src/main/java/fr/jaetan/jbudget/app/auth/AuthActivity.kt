package fr.jaetan.jbudget.app.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.jaetan.jbudget.app.MainActivity
import fr.jaetan.jbudget.app.auth.views.AuthScreen
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.ui.theme.JBudgetTheme

class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = this
            LaunchedEffect(Unit) { JBudget.init(context) }

            if (JBudget.state.isLogged) {
                startActivity(MainActivity.launch(this))
                finish()
            }

            JBudgetTheme(JBudget.state) {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = MaterialTheme.colorScheme.background
    val darkTheme = isSystemInDarkTheme()
    val viewModel = AuthViewModel()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = darkTheme
        )
        systemUiController.setSystemBarsColor(
            color = backgroundColor
        )
    }

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AuthScreen(viewModel)
    }
}