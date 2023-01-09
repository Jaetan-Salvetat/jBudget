package fr.jaetan.jbudget.core.services

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.jaetan.jbudget.core.models.Themes

class MainViewModel: ViewModel() {
    private var _currentTheme by mutableStateOf(Themes.System)
    private var _isNotificationEnabled by mutableStateOf(true)
    private val Context.settingsStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_KEY)

    val isLogged get() = currentUser != null
    val currentTheme: Themes get() = _currentTheme
    val isNotificationEnabled: Boolean get() = _isNotificationEnabled
    var currentUser by mutableStateOf(FirebaseAuth.getInstance().currentUser)

    suspend fun init(context: Context) {
        context.settingsStore.data.collect { prefs ->
            val theme = Themes.values().find { prefs[THEME_KEY] == it.text }
            val isNotifEnabled = prefs[IS_NOTIFICATION_ENABLED]

            Log.d("test", isNotifEnabled ?: "null")
            Log.d("test", "false".toBoolean().toString())

            theme?.let {
                _currentTheme = it
            }
            isNotifEnabled?.let {
                _isNotificationEnabled = it.toBoolean()
            }
        }
    }

    suspend fun saveTheme(context: Context, value: String) {
        context.settingsStore.edit {
            it[THEME_KEY] = value
        }
    }

    suspend fun notificationHandler(context: Context, value: Boolean) {
        context.settingsStore.edit {
            Log.d("testt", value.toString())
            it[IS_NOTIFICATION_ENABLED] = value.toString()
        }
    }

    companion object {
        private const val SETTINGS_KEY = "settings"
        private val THEME_KEY = stringPreferencesKey("theme")
        private val IS_NOTIFICATION_ENABLED = stringPreferencesKey("is_notification_enabled")
    }
}

