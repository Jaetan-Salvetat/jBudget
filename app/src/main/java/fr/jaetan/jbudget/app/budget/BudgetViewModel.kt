package fr.jaetan.jbudget.app.budget

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(budgetId: String?) : ViewModel() {
    var transactions = mutableStateListOf<Transaction>()
    var transactionLoadingState by mutableStateOf(State.Loading)
    var budget by mutableStateOf(null as Budget?)
    var expandCategories by mutableStateOf(false)

    @StringRes var firebaseResponse = null as Int?

    init {
        getBudget(budgetId)
        getTransactions(budgetId)
    }

    private fun getBudget(budgetId: String?) {
        budget = JBudget.state.budgets.find { it.id == budgetId }
    }

    private fun getTransactions(budgetId: String?) {
        JBudget.transactionRepository.getAll(budgetId) { transactions, response ->
            this.transactions.clear()
            this.transactions.addAll(transactions)

            when  {
                response == FirebaseResponse.Success && transactions.isEmpty() -> transactionLoadingState = State.EmptyData
                response == FirebaseResponse.Success -> transactionLoadingState = State.None
                else -> transactionLoadingState = State.Error
            }
        }
    }

    fun removeTransaction(transaction: Transaction) {
        JBudget.transactionRepository.removeTransaction(transaction.id) {
            if (it != FirebaseResponse.Success) return@removeTransaction
            transactions.remove(transaction)
        }
    }
}