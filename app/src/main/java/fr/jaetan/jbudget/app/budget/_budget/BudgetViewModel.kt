package fr.jaetan.jbudget.app.budget._budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.models.category
import fr.jaetan.jbudget.core.services.JBudget

class BudgetViewModel(private val budgetId: String?) : ViewModel() {
    private val _transactions get() = JBudget.state.budgets.find { it.id == budgetId }?.transactions ?: listOf()
    val categories get() = JBudget.state.categories
    val transactions get() = getTransactionsFiltered()
    var budget by mutableStateOf(null as Budget?)
    var budgetToRemove by mutableStateOf(null as Budget?)
    var budgetToEdit by mutableStateOf(null as Budget?)
    var showDropDownFilter by mutableStateOf(false)
    var filterType by mutableStateOf(FilterType.Date)
    var filterOrder by mutableStateOf(FilterOrder.Ascending)

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

    fun filterTypeHandler(type: FilterType) {
        filterType = type
    }

    fun filterOrderHandler(order: FilterOrder) {
        filterOrder = order
    }

    private fun getTransactionsFiltered(): List<Transaction> = when (filterType) {
        FilterType.Category -> filterByCategory()
        FilterType.Date -> filterByDate()
        FilterType.Spent -> filterBySpent()
    }

    private fun filterBySpent(): List<Transaction> = when (filterOrder) {
        FilterOrder.Ascending -> _transactions.sortedBy { it.amount }
        else -> _transactions.sortedByDescending { it.amount }
    }

    private fun filterByCategory(): List<Transaction> = when (filterOrder) {
        FilterOrder.Ascending -> _transactions.sortedBy { it.category?.name }
        else -> _transactions.sortedByDescending { it.category?.name }
    }

    private fun filterByDate(): List<Transaction> = when (filterOrder) {
        FilterOrder.Ascending -> _transactions.sortedBy { it.date }
        else -> _transactions.sortedByDescending { it.date }
    }
}

enum class FilterType {
    Spent,
    Category,
    Date
}

enum class FilterOrder {
    Descending,
    Ascending
}