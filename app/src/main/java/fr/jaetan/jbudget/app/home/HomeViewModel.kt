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
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Screen
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget

@OptIn(ExperimentalPagerApi::class)
class HomeViewModel(private val navController: NavHostController) : ViewModel() {
    // Home tips
    var showDeleteTipsButton by mutableStateOf(false)
    val tips = mutableStateListOf(
        TipsItem(R.string.long_press_to_remove_tips) { showDeleteTipsButton = true },
        TipsItem(R.string.change_app_theme) { navController.navigate(Screen.Settings.route) },
        TipsItem(R.string.can_no_specify_date) {},
        //manage default categories to settings view
    )

    suspend fun removeTips(pagerState: PagerState) {
        val index = pagerState.currentPage
        if (index == tips.size - 1) {
            pagerState.animateScrollToPage(index - 1)
        }
        tips.removeAt(index)
        showDeleteTipsButton = false
    }


    //Budgets
    var loadingState by mutableStateOf(State.Loading)
    var selectedOldBudget by mutableStateOf(null as Budget?)
    var selectedCurrentBudgets = mutableStateListOf<Budget>()
    private val budgets = mutableStateListOf<Budget>()
    val currentBudgets: List<Budget> get() = budgets.filter { it.isCurrentBudget }
    val oldBudgets: List<Budget> get() = budgets.filter { !it.isCurrentBudget }

    fun toggleSelectedBudget(budget: Budget) {
        if (budget.isCurrentBudget) {
            if (selectedCurrentBudgets.find { it == budget } != null) selectedCurrentBudgets.remove(budget)
            else selectedCurrentBudgets.add(budget)
            return
        }

        selectedOldBudget = if (budget == selectedOldBudget) null
        else budget
    }

    private fun initBudgets() {
        JBudget.budgetRepository.getAll { data, response ->
            loadingState = when {
                data.isEmpty() && response == FirebaseResponse.Success -> State.EmptyData
                response == FirebaseResponse.Success -> {
                    budgets.clear()
                    budgets.addAll(data)
                    selectedCurrentBudgets.addAll(currentBudgets)
                    State.None
                }
                else -> State.Error
            }
            if (budgets.isEmpty()) {
                tips.add(0, TipsItem(R.string.create_your_first_budget) { showNewBudgetDialog = true })
            }
        }
    }

    fun navigateToBudgetScreen(budgetId: String) {
        navController.navigate("${Screen.Budget.route}/$budgetId")
    }


    //FAB
    var showNewBudgetDialog by mutableStateOf(false)
    var showNewTransactionDialog by mutableStateOf(false)
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(
            text = R.string.home_fab_add_transaction,
            descriptor = R.string.home_fab_add_transaction_descriptor,
            onClick = {
                showNewTransactionDialog = true
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

    init { initBudgets() }
}

data class TipsItem(@StringRes val text: Int, val action: () -> Unit)

