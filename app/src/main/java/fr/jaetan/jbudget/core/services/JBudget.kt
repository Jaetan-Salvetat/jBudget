package fr.jaetan.jbudget.core.services

import fr.jaetan.jbudget.core.repositories.AuthRepository
import fr.jaetan.jbudget.core.repositories.UserRepository

class JBudget {
    companion object {
        var state = MainViewModel()

        val authRepository = AuthRepository()
        val userRepository = UserRepository()
    }
}