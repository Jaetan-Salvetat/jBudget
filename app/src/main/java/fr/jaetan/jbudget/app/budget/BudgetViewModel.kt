package fr.jaetan.jbudget.app.budget

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.*
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(budgetId: String?) : ViewModel() {
    var transactions = mutableStateListOf<Transaction>()
    var categories = mutableStateListOf<Category>()
    var transactionLoadingState by mutableStateOf(State.Loading)
    var budget by mutableStateOf(null as Budget?)
    var showNewCategoryDialog by mutableStateOf(false)

    @StringRes var firebaseResponse = null as Int?

    init {
        getBudget(budgetId)
        getTransactions(budgetId)
        getCategories(budgetId)
    }

    private fun getBudget(budgetId: String?) {
        budget = JBudget.state.budgets.find { it.id == budgetId }
    }

    private fun getTransactions(budgetId: String?) {
        JBudget.transactionRepository.getAll(budgetId) { transactions, response ->
            this.transactions.clear()
            this.transactions.addAll(transactions)

            transactionLoadingState = when  {
                response == FirebaseResponse.Success && transactions.isEmpty() -> State.EmptyData
                response == FirebaseResponse.Success -> State.None
                else -> State.Error
            }
        }
    }

    private fun getCategories(budgetId: String?) {
        JBudget.categoryRepository.getAll(budgetId) { categories, _ ->
            this.categories.clear()
            this.categories.addAll(categories)
        }
    }

    fun removeTransaction(transaction: Transaction) {
        JBudget.transactionRepository.removeTransaction(transaction.id) {
            if (it != FirebaseResponse.Success) return@removeTransaction
            transactions.remove(transaction)
        }
    }

    fun navigateToUpdateTransactionScreen(navController: NavHostController, transaction: Transaction) {
        navController.navigate("${Screen.Transaction.route}/${transaction.budgetId}/${transaction.id}/${transaction.amount}")
    }
}