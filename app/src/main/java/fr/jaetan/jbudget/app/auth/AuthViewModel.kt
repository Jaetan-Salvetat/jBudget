package fr.jaetan.jbudget.app.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.R

class AuthViewModel: ViewModel() {
    var currentScreen by mutableStateOf(AuthScreens.Login)
    var email by mutableStateOf(null as String?)
    var password by mutableStateOf(null as String?)
    var username by mutableStateOf(null as String?)


    fun navigate(screen: AuthScreens) {
        currentScreen = screen
        email = null
        password = null
        username = null

    }

    fun login() = Unit
    fun register() = Unit
}

enum class AuthScreens(
    @StringRes val titleRes: Int,
    @StringRes val navigationTextRes: Int,
    @StringRes val navigationTextActionRes: Int
    ) {
    Login(R.string.login, R.string.navigate_to_register, R.string.register),
    Register(R.string.register, R.string.navigate_to_login, R.string.login)
}