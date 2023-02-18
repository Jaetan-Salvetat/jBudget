package fr.jaetan.jbudget.app.budget.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
import java.util.Calendar
import java.util.Date

class CreateBudgetViewModel(dismiss: () -> Unit): ViewModel() {
    var newBudgetValue by mutableStateOf("")
    var newBudgetError by mutableStateOf(null as Int?)
    var newBudgetState by mutableStateOf(State.None)
    var startDate: Date by mutableStateOf(Calendar.getInstance().time)
    var endDate by mutableStateOf(null as Date?)
    private var dismiss: () -> Unit

    init {
        this.dismiss = {
            newBudgetValue = ""
            newBudgetError = null
            newBudgetState = State.None
            dismiss()
        }
    }

    fun createBudget() {
        newBudgetState = State.Loading
        val budget = Budget(
            name = newBudgetValue,
            startDate = startDate,
            endDate = endDate
        )
        JBudget.budgetRepository.createBudget(budget) { budgetId, response ->
            when (response) {
                FirebaseResponse.Error -> { newBudgetError = response.messageRes }
                FirebaseResponse.ConnectivityError -> { newBudgetError = response.messageRes }
                else -> {

                }
            }
        }
    }
}