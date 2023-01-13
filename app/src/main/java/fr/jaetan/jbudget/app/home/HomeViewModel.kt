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
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.views.FabItem
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Screen
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class HomeViewModel(private val navController: NavHostController) : ViewModel() {
    // Home tips
    var showDeleteTipsButton by mutableStateOf(false)
    val tips = mutableStateListOf(
        TipsItem(R.string.change_app_theme) { navController.navigate(Screen.Settings.route) },
        TipsItem(R.string.can_no_specify_date) {},
        //manage default categories to settings view
    )


    //Budgets
    var selectedOldBudget by mutableStateOf(null as Budget?)
    var selectedCurrentBudgets = mutableStateListOf<Budget>()
    private val budgets = mutableStateListOf(
        Budget(name = "Test 1"),
        Budget(name = "2023", startDate = Date.from(LocalDate.parse("2023-01-01").atStartOfDay().toInstant(ZoneOffset.UTC))),
        Budget(
            name = "Janvier 2023",
            startDate = Date.from(LocalDate.parse("2023-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
            endDate = Date.from(LocalDate.parse("2023-02-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
        ),
        Budget(
            name = "Décembre Jaetan 2022",
            startDate = Date.from(LocalDate.parse("2022-12-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
            endDate = Date.from(LocalDate.parse("2023-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
        ),
        Budget(
            name = "Novembre 2022",
            startDate = Date.from(LocalDate.parse("2022-11-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
            endDate = Date.from(LocalDate.parse("2022-12-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
        ),
        Budget(
            name = "Octobre 2022",
            startDate = Date.from(LocalDate.parse("2022-10-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
            endDate = Date.from(LocalDate.parse("2022-11-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
        ),
        Budget(
            name = "Septembre 2022",
            startDate = Date.from(LocalDate.parse("2022-09-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
            endDate = Date.from(LocalDate.parse("2022-10-01").atStartOfDay().toInstant(ZoneOffset.UTC)),
        ),
    )
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


    //FAB
    var showNewBudgetDialog by mutableStateOf(false)
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(text = R.string.home_fab_add_transaction, descriptor = R.string.home_fab_add_transaction_descriptor, onClick = {navController.navigate(
            Screen.Transaction.route)}, Icons.Default.RequestQuote),
        FabItem(text = R.string.home_fab_add_budget, descriptor = R.string.home_fab_add_budget_descriptor, onClick = { showNewBudgetDialog = !showNewBudgetDialog}, Icons.Default.NoteAdd),
    )


    init {
        selectedCurrentBudgets.addAll(currentBudgets)
        //Dans le callback de la récupération des budgets
        if (budgets.isEmpty()) {
            tips.add(0, TipsItem(R.string.create_your_first_budget) { showNewBudgetDialog = true })
        }
    }
}

data class TipsItem(@StringRes val text: Int, val action: () -> Unit)