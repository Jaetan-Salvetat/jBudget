package fr.jaetan.jbudget.app.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.views.FabItem
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget

class HomeViewModel(navController: NavHostController) : ViewModel() {
    var newBudgetValue by mutableStateOf("")
    var newBudgetError by mutableStateOf(null as Int?)
    var newBudgetState by mutableStateOf(State.None)
    var showNewBudgetDialog by mutableStateOf(false)
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(text = R.string.home_fab_add_transaction, descriptor = R.string.home_fab_add_transaction_descriptor, onClick = {navController.navigate(
            Screen.Transaction.route)}, Icons.Default.RequestQuote),
        FabItem(text = R.string.home_fab_add_budget, descriptor = R.string.home_fab_add_budget_descriptor, onClick = { showNewBudgetDialog = !showNewBudgetDialog}, Icons.Default.NoteAdd),
    )

    fun createBudget(budgetName: String) {
        JBudget.budgetRepository.createBudget(budgetName) { _, response ->
            when (response) {
                FirebaseResponse.Error -> {newBudgetError = response.messageRes }
                FirebaseResponse.ConnectivityError -> {newBudgetError = response.messageRes }
                else -> {
                    newBudgetState = State.None
                    newBudgetValue = ""
                    showNewBudgetDialog = false
                }
            }
        }
    }
}