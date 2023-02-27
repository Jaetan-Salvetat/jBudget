package fr.jaetan.jbudget.app.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword

class AuthViewModel(navDestination: NavDestination?): ViewModel() {
    val currentScreen = AuthScreens.values().find { navDestination?.route == it.route } ?: AuthScreens.Login
    private var _email by mutableStateOf(null as String?)
    private var _password by mutableStateOf(null as String?)
    var state by mutableStateOf(State.None)
    var showForgotPasswordDialog by mutableStateOf(false)
    var email: String?
        get() = _email
        set(value) {
            _email = value
            state = State.None
        }
    var password: String?
        get() = _password
        set(value) {
            _password = value
            state = State.None
        }

    @StringRes var errorMessageRes: Int = R.string.sample_error
    val canContinue: Boolean
        get() = password?.isPassword == true
            && email?.isEmail == true
            && state == State.None

    fun auth() {
        if (!canContinue) return
        state = State.Loading
        if (currentScreen == AuthScreens.Login) return login()
        register()
    }

    private fun login() {
        JBudget.authRepository.loginWithEmailAndPassword(email!!, password!!) {
            errorMessageRes = it.messageRes

            state = when (it) {
                FirebaseResponse.Success -> State.None
                else -> State.Error
            }
        }
    }

    private fun register() {
        JBudget.authRepository.registerWithEmailAndPassword(email!!, password!!) {
            errorMessageRes = it.messageRes

            state = when (it) {
                FirebaseResponse.Success -> State.None
                else -> State.Error
            }
        }
    }
}

enum class AuthScreens(
    val route: String,
    @StringRes val titleRes: Int,
    @StringRes val navigationTextRes: Int,
    @StringRes val navigationTextActionRes: Int
    ) {
    Login("login", R.string.login, R.string.navigate_to_register, R.string.register),
    Register("register", R.string.register, R.string.navigate_to_login, R.string.login)
}