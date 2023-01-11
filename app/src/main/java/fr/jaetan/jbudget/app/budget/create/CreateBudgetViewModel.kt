package fr.jaetan.jbudget.app.budget.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget

class CreateBudgetViewModel(dismiss: () -> Unit): ViewModel() {
    var newBudgetValue by mutableStateOf("")
    var newBudgetError by mutableStateOf(null as Int?)
    var newBudgetState by mutableStateOf(State.None)
    var dismiss: () -> Unit

    init {
        this.dismiss = {
            newBudgetValue = ""
            newBudgetError = null
            newBudgetState = State.None
            dismiss()
        }
    }

    fun createBudget(budgetName: String) {
        JBudget.budgetRepository.createBudget(budgetName) { _, response ->
            when (response) {
                FirebaseResponse.Error -> { newBudgetError = response.messageRes }
                FirebaseResponse.ConnectivityError -> { newBudgetError = response.messageRes }
                else -> {
                    newBudgetState = State.None
                    newBudgetValue = ""
                    dismiss()
                }
            }
        }
    }
}