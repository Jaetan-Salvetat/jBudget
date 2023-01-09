package fr.jaetan.jbudget.app.budget

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(navController: NavHostController, budgetId: String?) : ViewModel() {
    var budget by mutableStateOf(null as Budget?)
    @StringRes var firebaseResponse = null as Int?

    init {
        JBudget.budgetRepository.getBudget(budgetId) { budget, response ->

            when (response) {
                FirebaseResponse.Success -> { this.budget = budget }
                else -> {
                    firebaseResponse = response.messageRes
                }
            }
        }
    }
}