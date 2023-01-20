package fr.jaetan.jbudget.app.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword
import fr.jaetan.jbudget.core.services.extentions.isUsename

class AuthViewModel: ViewModel() {
    var currentScreen by mutableStateOf(AuthScreens.Login)
    private var _email by mutableStateOf(null as String?)
    private var _password by mutableStateOf(null as String?)
    private var _username by mutableStateOf(null as String?)
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
    var username: String?
        get() = _username
        set(value) {
            _username = value
            state = State.None
        }

    @StringRes var errorMessageRes: Int = R.string.sample_error
    val canContinue: Boolean
        get() = password?.isPassword == true
            && email?.isEmail == true
            && (username?.isUsename == true || currentScreen == AuthScreens.Login)
            && state == State.None


    fun navigate(screen: AuthScreens) {
        currentScreen = screen
        email = null
        password = null
        username = null

    }

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
        JBudget.authRepository.registerWithEmailAndPassword(email!!, username!!, password!!) {
            errorMessageRes = it.messageRes

            state = when (it) {
                FirebaseResponse.Success -> State.None
                else -> State.Error
            }
        }
    }
}

enum class AuthScreens(
    @StringRes val titleRes: Int,
    @StringRes val navigationTextRes: Int,
    @StringRes val navigationTextActionRes: Int
    ) {
    Login(R.string.login, R.string.navigate_to_register, R.string.register),
    Register(R.string.register, R.string.navigate_to_login, R.string.login)
}