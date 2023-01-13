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
    val budgets = mutableStateListOf(
        Budget(id = "", name = "Name 1", startDate = Date.from(LocalDate.parse("2022-12-01").atStartOfDay().toInstant(ZoneOffset.UTC))),
        Budget(id = "", name = "Name 2", startDate = Date.from(LocalDate.parse("2023-01-01").atStartOfDay().toInstant(ZoneOffset.UTC))),
    )
    var selectedBudget by mutableStateOf(null as Budget?)

    fun toggleSelectedBudget(budget: Budget) {
        selectedBudget = if (budget == selectedBudget) null
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
        //Dans le callback de la récupération des budgets
        if (budgets.isEmpty()) {
            tips.add(0, TipsItem(R.string.create_your_first_budget) { showNewBudgetDialog = true })
        }
    }
}

data class TipsItem(@StringRes val text: Int, val action: () -> Unit)