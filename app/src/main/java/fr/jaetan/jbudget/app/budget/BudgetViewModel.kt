package fr.jaetan.jbudget.app.budget

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.*
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(private val budgetId: String?) : ViewModel() {
    val transactions get() = JBudget.state.budgets.find { it.id == budgetId }?.transactions ?: listOf()
    val categories get() = JBudget.state.budgets.find { it.id == budgetId }?.categories ?: listOf()
    var transactionLoadingState by mutableStateOf(State.Loading)
    var budget by mutableStateOf(null as Budget?)
    var showNewCategoryDialog by mutableStateOf(false)
    var isEditable by mutableStateOf(false)

    @StringRes var firebaseResponse = null as Int?

    init {
        getBudget(budgetId)
    }

    private fun getBudget(budgetId: String?) {
        budget = JBudget.state.budgets.find { it.id == budgetId }
    }

    fun removeTransaction(transaction: Transaction) {
        JBudget.transactionRepository.removeTransaction(transaction.id) { response ->
            if (response != FirebaseResponse.Success) return@removeTransaction
            JBudget.state.budgets.find { it.id ==  budgetId}?.transactions?.remove(transaction)
        }
    }

    fun navigateToUpdateTransactionScreen(navController: NavHostController, transaction: Transaction) {
        navController.navigate("${Screen.Transaction.route}/${transaction.budgetId}/${transaction.id}/${transaction.amount}")
    }
}