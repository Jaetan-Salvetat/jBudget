package fr.jaetan.jbudget.app.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.models.Themes
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.isEmail
import fr.jaetan.jbudget.core.services.extentions.isPassword
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO): ViewModel() {
    var showThemeDropDown by mutableStateOf(false)

    fun changeTheme(context: Context, theme: Themes) {
        showThemeDropDown = false
        viewModelScope.launch(dispatcher) {
            JBudget.state.saveTheme(context, theme.text)
        }
    }

    fun notificationHandler(context: Context, isEnabled: Boolean) {
        viewModelScope.launch(dispatcher) {
            JBudget.state.notificationHandler(context, isEnabled)
        }
    }


    // Update password dialog
    var showResetPasswordDialog by mutableStateOf(false)


    // Update email dialog
    var showUpdateEmailDialog by mutableStateOf(false)
    var email by mutableStateOf(null as String?)
    var password by mutableStateOf(null as String?)
    var currentEmail by mutableStateOf(JBudget.state.currentUser!!.email)
    var updateEmailState by mutableStateOf(State.None)
    var updateEmailErrorMessageRes by mutableStateOf(null as Int?)

    fun updateEmail() {
        if (email?.isEmail == false && password?.isPassword == false || updateEmailState == State.Loading) return
        updateEmailState = State.Loading

        JBudget.userRepository.updateEmail(email!!, password!!) {
            updateEmailState = State.None
            if (it != FirebaseResponse.Success) {
                updateEmailErrorMessageRes = it.messageRes
                return@updateEmail
            }

            currentEmail = JBudget.state.currentUser!!.email
            dismissUpdateEmailDialog()
        }
    }

    fun dismissUpdateEmailDialog() {
        showUpdateEmailDialog = false
        email = null
        password = null
        updateEmailErrorMessageRes = null
    }
}
