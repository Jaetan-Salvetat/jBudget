package fr.jaetan.jbudget.app.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(private val budgetId: String?) : ViewModel() {
    val transactions get() = JBudget.state.budgets.find { it.id == budgetId }?.transactions ?: listOf()
    val categories get() = JBudget.state.budgets.find { it.id == budgetId }?.categories ?: listOf()
    var budget by mutableStateOf(null as Budget?)
    var isEditable by mutableStateOf(false)

    init { getBudget(budgetId) }

    private fun getBudget(budgetId: String?) {
        budget = JBudget.state.budgets.find { it.id == budgetId }
    }

    fun removeTransaction(transaction: Transaction) {
        JBudget.transactionRepository.removeTransaction(transaction.id)
    }

    fun navigateToUpdateTransactionScreen(navController: NavHostController, transaction: Transaction) {
        navController.navigate("${Screen.Transaction.route}/${transaction.id}")
    }
}