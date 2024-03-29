package fr.jaetan.jbudget.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import fr.jaetan.jbudget.app.auth.AuthActivity
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.ui.theme.JBudgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LaunchedEffect(Unit) { JBudget.init(this@MainActivity) }
            JBudget.initFireStore()

            LaunchedEffect(Unit) { JBudget.budgetRepository.initListener() }
            LaunchedEffect(Unit) { JBudget.categoryRepository.initListener() }
            JBudget.state.budgets.forEach {
                LaunchedEffect(Unit) { JBudget.transactionRepository.initListener(it.id) }
            }

            if (!JBudget.state.isLogged) {
                startActivity(AuthActivity.launch(this))
                finish()
            }

            JBudgetTheme(JBudget.state) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }

    companion object {
        fun launch(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}