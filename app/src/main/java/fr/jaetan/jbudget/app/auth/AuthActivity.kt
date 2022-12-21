package fr.jaetan.jbudget.app.auth

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.jaetan.jbudget.app.auth.views.AuthScreen

class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContent {
            AuthScreen()
        }
    }
}