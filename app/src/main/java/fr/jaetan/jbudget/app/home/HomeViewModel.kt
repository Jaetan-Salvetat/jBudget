package fr.jaetan.jbudget.app.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.views.FabItem
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget

@OptIn(ExperimentalPagerApi::class)
class HomeViewModel(private val navController: NavHostController) : ViewModel() {
    // Home tips
    var showDeleteTipsButton by mutableStateOf(false)
    val tips = mutableStateListOf<TipsItem>()

    suspend fun removeTips(pagerState: PagerState) {
        val index = pagerState.currentPage
        if (index == tips.size - 1) {
            pagerState.animateScrollToPage(index - 1)
        }
        tips.removeAt(index)
        showDeleteTipsButton = false
    }


    //Budgets
    var selectedOldBudget by mutableStateOf(null as Budget?)
    var selectedCurrentBudgets = mutableStateListOf<Budget>()
    val currentBudgets: List<Budget> get() = JBudget.state.budgets.filter { it.isCurrentBudget }.sortedByDescending { it.startDate }
    val oldBudgets: List<Budget> get() = JBudget.state.budgets.filter { !it.isCurrentBudget }.sortedByDescending { it.startDate }
    val loadingState: State get() = JBudget.state.budgetsLoadingState
    var budgetToRemove by mutableStateOf(null as Budget?)
    var budgetToEdit by mutableStateOf(null as Budget?)

    fun toggleSelectedBudget(budget: Budget) {
        if (budget.isCurrentBudget) {
            if (selectedCurrentBudgets.find { it == budget } != null) selectedCurrentBudgets.remove(budget)
            else selectedCurrentBudgets.add(budget)
            return
        }

        selectedOldBudget = if (budget == selectedOldBudget) null
        else budget
    }

    fun navigateToBudgetScreen(budgetId: String) {
        navController.navigate("${Screen.Budget.route}/$budgetId")
    }

    fun navigateToTransactionScreen(budgetId: String) {
        navController.navigate("${Screen.Transaction.route}/budget/$budgetId")
    }

    //FAB
    var showNewBudgetDialog by mutableStateOf(false)
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(
            text = R.string.home_fab_add_transaction,
            descriptor = R.string.home_fab_add_transaction_descriptor,
            onClick = {
                navController.navigate(Screen.Transaction.route)
                fabExpanded = false
            },
            icon = Icons.Default.RequestQuote
        ),
        FabItem(
            text = R.string.home_fab_add_budget,
            descriptor = R.string.home_fab_add_budget_descriptor,
            onClick = {
                showNewBudgetDialog = !showNewBudgetDialog
                fabExpanded = false
            },
            icon = Icons.Default.NoteAdd
        ),
    )

    init {
        currentBudgets.firstOrNull()?.let {
            selectedCurrentBudgets.add(it)
        }

        if (JBudget.state.budgets.isEmpty()) {
            tips.add(0, TipsItem(R.string.create_your_first_budget) { showNewBudgetDialog = true })
        }
        if (!JBudget.state.hasChangeTheme) {
            tips.add(TipsItem(R.string.change_app_theme) { navController.navigate(Screen.Settings.route) })
        }
    }
}

data class TipsItem(@StringRes val text: Int, val action: () -> Unit)

