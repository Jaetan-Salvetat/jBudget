package fr.jaetan.jbudget.core.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import fr.jaetan.jbudget.core.repositories.AuthRepository
import fr.jaetan.jbudget.core.repositories.UserRepository

class JBudget {
    companion object {
        var isLogged by mutableStateOf(FirebaseAuth.getInstance().currentUser != null)

        val authRepository = AuthRepository()
        val userRepository = UserRepository()
    }
}