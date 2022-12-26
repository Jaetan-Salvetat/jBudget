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
    var email by mutableStateOf(null as String?)
    var password by mutableStateOf(null as String?)
    var username by mutableStateOf(null as String?)
    var state by mutableStateOf(State.None)
    @StringRes var errorMessageRes: Int = R.string.sample_error
    val canContinue: Boolean
        get() = password?.isPassword == true
            && email?.isEmail == true
            && (username?.isUsename == true || currentScreen == AuthScreens.Login)


    fun navigate(screen: AuthScreens) {
        currentScreen = screen
        email = null
        password = null
        username = null

    }

    fun auth() {
        state = State.Loading
        if (currentScreen == AuthScreens.Login) return login()
        register()
    }

    private fun login() {
        JBudget.authRepository.loginWithEmailAndPassword(email!!, password!!) {
            when (it) {
                FirebaseResponse.Success -> {
                    state = State.None
                    JBudget.isLogged = true
                }
                FirebaseResponse.BadEmailOrPassword -> {
                    state = State.Error
                    errorMessageRes = R.string.bad_email_or_password
                }
                else -> {
                    errorMessageRes = R.string.sample_error
                    state = State.Error
                }
            }
        }
    }

    private fun register() {
        JBudget.authRepository.registerWithEmailAndPassword(email!!, username!!, password!!) {
            when (it) {
                FirebaseResponse.Success -> {
                    state = State.None
                    JBudget.isLogged = true
                }
                FirebaseResponse.UserAlreadyExist -> {
                    errorMessageRes = R.string.user_already_exist
                    state = State.Error
                }
                else -> {
                    errorMessageRes = R.string.sample_error
                    state = State.Error
                }
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