package fr.jaetan.jbudget.app.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.jaetan.jbudget.core.models.Themes
import fr.jaetan.jbudget.core.services.JBudget
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
}
