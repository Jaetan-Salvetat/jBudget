package fr.jaetan.jbudget.core.services

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.models.Themes

class MainViewModel: ViewModel() {
    private var _currentTheme by mutableStateOf(Themes.Dark)
    private var _isNotificationEnabled by mutableStateOf(true)
    private val Context.settingsStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_KEY)

    val isLogged get() = currentUser != null
    val currentTheme: Themes get() = _currentTheme
    val isNotificationEnabled: Boolean get() = _isNotificationEnabled
    val budgets = mutableStateListOf<Budget>()
    val categories = mutableStateListOf<Category>()
    var currentUser by mutableStateOf(null as FirebaseUser?)
    var budgetsLoadingState by mutableStateOf(State.Loading)

    suspend fun init(context: Context) {
        currentUser = FirebaseAuth.getInstance().currentUser
        initBudgets()
        FirebaseAuth.getInstance().addAuthStateListener {
            currentUser = it.currentUser
        }

        context.settingsStore.data.collect { prefs ->
            val theme = Themes.values().find { prefs[THEME_KEY] == it.textRes.toString() }
            val isNotifEnabled = prefs[IS_NOTIFICATION_ENABLED]

            theme?.let {
                _currentTheme = it
            }
            isNotifEnabled?.let {
                _isNotificationEnabled = it.toBoolean()
            }
        }
    }

    suspend fun saveTheme(context: Context, value: Int) {
        context.settingsStore.edit {
            it[THEME_KEY] = value.toString()
        }
    }

    suspend fun notificationHandler(context: Context, value: Boolean) {
        context.settingsStore.edit {
            it[IS_NOTIFICATION_ENABLED] = value.toString()
        }
    }


    private fun initBudgets() {
        /*JBudget.budgetRepository.getAll { data, response ->
            budgetsLoadingState = when {
                data.isEmpty() && response == FirebaseResponse.Success -> State.EmptyData
                response == FirebaseResponse.Success -> {
                    budgets.clear()
                    budgets.addAll(data)
                    State.None
                }
                else -> State.Error
            }
        }*/
    }

    companion object {
        private const val SETTINGS_KEY = "settings"
        private val THEME_KEY = stringPreferencesKey("theme")
        private val IS_NOTIFICATION_ENABLED = stringPreferencesKey("is_notification_enabled")
    }
}

