package fr.jaetan.jbudget.core.services

import android.content.Context
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
    private val Context.themeStore: DataStore<Preferences> by preferencesDataStore(THEME_KEY.name)

    val currentTheme: Themes get() = _currentTheme
    var isLogged by mutableStateOf(FirebaseAuth.getInstance().currentUser != null)

    suspend fun init(context: Context) {
        context.themeStore.data.collect { prefs ->
            val theme = Themes.values().find { prefs[THEME_KEY] == it.text }
            theme?.let {
                _currentTheme = it
            }
        }
    }

    suspend fun saveTheme(context: Context, value: String) {
        context.themeStore.edit {
            it[THEME_KEY] = value
        }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }
}

