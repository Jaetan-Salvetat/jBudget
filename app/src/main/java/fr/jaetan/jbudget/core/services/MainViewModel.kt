package fr.jaetan.jbudget.core.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.jaetan.jbudget.core.models.Themes

class MainViewModel: ViewModel() {
    private var _currentTheme by mutableStateOf(Themes.System)
    var currentTheme: Themes
        get() = _currentTheme
        set(value) { _currentTheme = value }
    var isLogged by mutableStateOf(FirebaseAuth.getInstance().currentUser != null)}