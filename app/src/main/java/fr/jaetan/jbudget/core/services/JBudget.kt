package fr.jaetan.jbudget.core.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class JBudget {
    companion object {
        var isLogged by mutableStateOf(false)
    }
}