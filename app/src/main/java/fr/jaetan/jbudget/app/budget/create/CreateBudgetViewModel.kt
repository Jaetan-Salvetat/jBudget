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

class CreateBudgetViewModel(private val budget: Budget?, dismiss: () -> Unit): ViewModel() {
    var newBudgetValue by mutableStateOf(budget?.name ?: "")
    var newBudgetError by mutableStateOf(null as Int?)
    var newBudgetState by mutableStateOf(State.None)
    var startDate: Date by mutableStateOf(budget?.startDate ?: Calendar.getInstance().time)
    var endDate by mutableStateOf(budget?.endDate)
    private var dismiss: () -> Unit
    val isInEditMode = budget != null

    init {
        this.dismiss = {
            newBudgetValue = ""
            newBudgetError = null
            newBudgetState = State.None
            dismiss()
        }
    }

    fun editBudget() {
        newBudgetState = State.Loading

        JBudget.budgetRepository.edit(budget!!.copy(name = newBudgetValue, startDate = startDate, endDate = endDate)) {
            newBudgetError = it.messageRes
            newBudgetState = if (it == FirebaseResponse.Success) State.None else State.Error
        }
    }

    fun createBudget() {
        newBudgetState = State.Loading
        val budget = Budget(
            name = newBudgetValue,
            startDate = startDate,
            endDate = endDate
        )
        JBudget.budgetRepository.createBudget(budget) { _, response ->
            newBudgetError = response.messageRes
            newBudgetState = if (response == FirebaseResponse.Success) State.None else State.Error
        }
    }
}