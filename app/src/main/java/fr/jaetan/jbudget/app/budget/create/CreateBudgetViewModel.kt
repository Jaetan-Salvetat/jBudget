package fr.jaetan.jbudget.app.budget.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget
import java.util.*

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

    fun createBudget(navController: NavHostController) {
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
                    JBudget.state.budgets.add(budget.copy(id = budgetId!!))
                    navController.navigate("${Screen.Budget.route}/$budgetId")
                }
            }
        }
    }
}